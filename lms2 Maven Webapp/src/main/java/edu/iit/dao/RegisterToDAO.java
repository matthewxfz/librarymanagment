package edu.iit.dao;

import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * RegisterTo entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see edu.iit.dao.RegisterTo
 * @author MyEclipse Persistence Tools
 */
public class RegisterToDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(RegisterToDAO.class);
	// property constants

	public void save(RegisterTo transientInstance) {
		log.debug("saving RegisterTo instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(RegisterTo persistentInstance) {
		log.debug("deleting RegisterTo instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RegisterTo findById(edu.iit.dao.RegisterToId id) {
		log.debug("getting RegisterTo instance with id: " + id);
		try {
			RegisterTo instance = (RegisterTo) getSession().get("edu.iit.dao.RegisterTo", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(RegisterTo instance) {
		log.debug("finding RegisterTo instance by example");
		try {
			List results = getSession().createCriteria("edu.iit.dao.RegisterTo").add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding RegisterTo instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from RegisterTo as model where model." + propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all RegisterTo instances");
		try {
			String queryString = "from RegisterTo";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public RegisterTo merge(RegisterTo detachedInstance) {
		log.debug("merging RegisterTo instance");
		try {
			RegisterTo result = (RegisterTo) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(RegisterTo instance) {
		log.debug("attaching dirty RegisterTo instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RegisterTo instance) {
		log.debug("attaching clean RegisterTo instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<RegisterTo> findByStudentID(String value, int pageNumber, int pageSize) {
		//log.debug("finding RegisterTo instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from RegisterTo where StudentID= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			queryObject.setFirstResult((pageNumber - 1) * pageSize);
			queryObject.setMaxResults(pageSize);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
}