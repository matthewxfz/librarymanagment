package edu.iit.dao;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Orders entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see edu.iit.dao.Orders
 * @author MyEclipse Persistence Tools
 */
public class OrdersDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(OrdersDAO.class);
	// property constants
	public static final String TYPE = "type";
	public static final String STATUES = "statues";

	public void save(Orders transientInstance) throws Exception{
		log.debug("saving Orders instance");
		try {
			Students student =new Students();
			student =transientInstance.getStudents();
			RegisterToDAO rdao= new RegisterToDAO();
			OrdersDAO odao= new OrdersDAO();
			StudentsDAO sudao= new StudentsDAO();
			int capacity= sudao.booktoborrow(student.getStudentId());
			List bookrent = odao.findALLBooks_book_ByStudentId(1, 20, student.getStudentId());
			if(capacity>bookrent.size()){
				getSession().save(transientInstance);
				log.debug("save successful");
			}else{
				throw new Exception("Out of Capacity");
				//System.out.println("Out of Capacity");
			}
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Orders persistentInstance) {
		log.debug("deleting Orders instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Orders findById(java.lang.Integer id) {
		log.debug("getting Orders instance with id: " + id);
		try {
			Orders instance = (Orders) getSession().get("edu.iit.dao.Orders", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	public void update_order(int bookid,int studentid){
		//Orders or= this.fin
		try {
			String queryString = "from Orders where BookID =? and Statues = ? and StudentID = ?";
			Query queryObject = getSession().createQuery(queryString);
			Transaction tx = getSession().beginTransaction();
			Object value = "Open";
			queryObject.setParameter(0, bookid);
			queryObject.setParameter(1, "Open");
			queryObject.setParameter(2, studentid);
			List<Orders> list = queryObject.list();
			Orders temp_order = list.get(0);
			ZoneId zonedId = ZoneId.of( "America/Chicago" );
			LocalDate ltoday = LocalDate.now( zonedId );
			java.util.Date today = java.sql.Date.valueOf(ltoday);
			temp_order.setCheckoutDate(today);
			temp_order.setStatues("Closed");
			tx.commit();
			getSession().close();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}			
	}
	public List findByExample(Orders instance) {
		log.debug("finding Orders instance by example");
		try {
			List results = getSession().createCriteria("edu.iit.dao.Orders").add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Orders instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from Orders as model where model." + propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List countdue(String propertyName, Object value) {
		// log.debug("finding Orders instance with property: " + propertyName +
		// ", value: " + value);
		try {
			String queryString = "select StudentID,count(BookID), BookID from Orders where DueDate <CheckoutDate group by BookID, StudentID;";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			// log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByType(Object type) {
		return findByProperty(TYPE, type);
	}

	public List findByStatues(Object statues) {
		return findByProperty(STATUES, statues);
	}

	public List findAll() {
		log.debug("finding all Orders instances");
		try {
			String queryString = "from Orders";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Orders merge(Orders detachedInstance) {
		log.debug("merging Orders instance");
		try {
			Orders result = (Orders) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Orders instance) {
		log.debug("attaching dirty Orders instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Orders instance) {
		log.debug("attaching clean Orders instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public List findAllDueBooks(int pageNumber, int pageSize) {

		try {
			String queryString = "from Orders where Statues =? and Date(now()) > DueDate";
			Query queryObject = getSession().createQuery(queryString);
			Object value = "Open";
			queryObject.setParameter(0, value);
			queryObject.setFirstResult((pageNumber - 1) * pageSize);// 显示第几页，当前页
			queryObject.setMaxResults(pageSize);// 每页做多显示的记录数
			List<Orders> list = queryObject.list();
			return list;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}

	}

	public List findDueBooksByStudentId(int pageNumber, int pageSize, Students sd) {
		try {
			String queryString = "from Orders where Statues =? and Date(now()) > DueDate and StudentID = ?";
			Query queryObject = getSession().createQuery(queryString);
			Object value = "Open";
			queryObject.setParameter(0, value);
			queryObject.setParameter(1, sd.getStudentId());
			queryObject.setFirstResult((pageNumber - 1) * pageSize);// 显示第几页，当前页
			queryObject.setMaxResults(pageSize);// 每页做多显示的记录数
			List<Orders> list = queryObject.list();
			return list;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAllBooksByStudentId(int pageNumber, int pageSize, Integer studentId) {
		try {
			String queryString = "from Orders where StudentID = ? and Statues = ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, studentId);
			queryObject.setParameter(1, "Open");
			queryObject.setFirstResult((pageNumber - 1) * pageSize);// 显示第几页，当前页
			queryObject.setMaxResults(pageSize);// 每页做多显示的记录数
			List<Orders> list = queryObject.list();
			return list;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findALLBooks_book_ByStudentId(int pageNumber, int pageSize, Integer studentId) {
		try {
			List<Orders> overdue_order = findAllBooksByStudentId(pageNumber, pageSize, studentId);
			List<Books> overdue_book = new ArrayList<Books>();
			for (int i = 0; i < overdue_order.size(); i++) {
				overdue_book.add(overdue_order.get(i).getBooks());
			}
			return overdue_book;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findDueBooks_book_ByStudentId(int pageNumber, int pageSize, Students sd) {
		try {
			List<Orders> overdue_order = findDueBooksByStudentId(pageNumber, pageSize, sd);
			List<Books> overdue_book = new ArrayList<Books>();
			for (int i = 0; i < overdue_order.size(); i++) {
				overdue_book.add(overdue_order.get(i).getBooks());
			}
			return overdue_book;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAllrentBooks(int pageNumber, int pageSize) {

		try {
			String queryString = "from Orders where Statues =?";
			Query queryObject = getSession().createQuery(queryString);
			Object value = "Open";
			queryObject.setParameter(0, value);
			queryObject.setFirstResult((pageNumber - 1) * pageSize);// 显示第几页，当前页
			queryObject.setMaxResults(pageSize);// 每页做多显示的记录数
			List<Orders> list = queryObject.list();
			return list;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}

	}

	public List find_overdue_student(int pageNumber, int pageSize) {

		try {

			List<Orders> overdue_order = findAllDueBooks(pageNumber, pageSize);
			List<Students> overdue_student = new ArrayList<Students>();
			for (int i = 0; i < overdue_order.size(); i++) {
				overdue_student.add(overdue_order.get(i).getStudents());
			}
			return overdue_student;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}

	}

	public List check_overdue_book(int pageNumber, int pageSize) {
		try {
			List<Orders> overdue_order = findAllDueBooks(pageNumber, pageSize);
			List<Books> overdue_book = new ArrayList<Books>();
			for (int i = 0; i < overdue_order.size(); i++) {
				overdue_book.add(overdue_order.get(i).getBooks());
			}
			return overdue_book;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}

	}

}
