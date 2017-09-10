package com.jh.base;

import org.hibernate.*;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

@Transactional
public class BaseDaoImlp<T> implements BaseDao<T> {

	@Autowired
	protected SessionFactory sessionFactory;

	private Class<T> targetClass;

	public BaseDaoImlp() {
		// 获取类型
		ParameterizedType type = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		targetClass = (Class<T>) type.getActualTypeArguments()[0];
	}
	public BaseDaoImlp(SessionFactory sessionFactory) {
		// 获取类型
	this.setSessionFactory(sessionFactory);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void save(T t) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(t);
	}

	@Override
	public void update(T t) {
		Session session = sessionFactory.getCurrentSession();
		session.update(t);
	}

	@Override
	public void merge(T t) {
		Session session = sessionFactory.getCurrentSession();
		session.merge(t);

	}

	@Override
	public void saveOrUpdate(T t) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(t);
	}

	@Override
	public void delete(T t) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(t);
	}

	@Override
	public List<T> findAll() {
		Session session = sessionFactory.getCurrentSession();
		return session.createCriteria(targetClass).list();
	}

	@Override
	public T findById(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		return (T) session.get(targetClass, id);
	}

	@Override
	public List<T> findByProperty(String propertyName, Object value) {
		Session session = sessionFactory.getCurrentSession();
		String queryString = "from " + targetClass.getName()
				+ " as model where model." + propertyName + "=:propertyName";
		Query queryObject = session.createQuery(queryString);
		queryObject.setParameter("propertyName", value);
		return queryObject.list();
	}

	@Override
	public List<T> findByPropertyWithValid(String propertyName, Object value) {
		Session session = sessionFactory.getCurrentSession();
		String queryString = "from " + targetClass.getName()
				+ " as model where model." + propertyName
				+ "=:propertyName and model.isValid = true";
		Query queryObject = session.createQuery(queryString);
		queryObject.setParameter("propertyName", value);
		return queryObject.list();
	}

	@Override
	public Integer findCountByQueryChar(String queryChar) {
		Session session = sessionFactory.getCurrentSession();
		String queryString = "select count(id) from " + targetClass.getName()
				+ " where " + queryChar;
		Query queryObject = session.createQuery(queryString);
		return Integer.parseInt(queryObject.uniqueResult().toString());
	}

	@Override
	public List<T> findListByQueryChar(String queryChar, Integer startIndex,
			Integer pageSize) {
		Session session = sessionFactory.getCurrentSession();
		String queryString = "from " + targetClass.getName() + " where "
				+ queryChar;
		queryString = queryString + " order by id desc";
		Query queryObject = session.createQuery(queryString);
		queryObject.setFirstResult(startIndex);
		queryObject.setMaxResults(pageSize);
		return queryObject.list();
	}

	@Override
	public List<T> findListByQueryChar(String queryChar) {
		Session session = sessionFactory.getCurrentSession();
		String queryString = "from " + targetClass.getName() + " where "
				+ queryChar;
		queryString = queryString + " order by id desc";
		Query queryObject = session.createQuery(queryString);
		return queryObject.list();
	}

	public void updatePropertyByID(String propertyName, Object value, int id,
			Class<T> model) {
		String className = model.getName();
		String queryString = "update " + className + " set " + propertyName+" = ?0 where id = ?1";
		Session session = sessionFactory.getCurrentSession();
		Query queryObject = session.createQuery(queryString);
		queryObject.setParameter("0", value);
		queryObject.setInteger("1", id);
		queryObject.executeUpdate();
	}

	public List<T> findByCon(Class<T> model, String condition, int start,
			int end) {
		// Integer count = 0;
		Session session = getSessionFactory().getCurrentSession();
		try {

			String queryString = "from " + model.getName() + " where "
					+ condition;

			Query queryObject = session.createQuery(queryString);
			queryObject.setFirstResult(start);
			queryObject.setMaxResults(end);
			return queryObject.list();
		} catch (RuntimeException re) {
			if (session.isConnected() || session.isOpen()) {
				session.close();
			}
			String queryString = "from " + model.getName() + " where "
					+ condition;

			Query queryObject = session.createQuery(queryString);
			queryObject.setFirstResult(start);
			queryObject.setMaxResults(end);
			return queryObject.list();
		}
	}

	public List<T> findByCon(Class<T> model, String condition) {
		// Integer count = 0;
		Session session = getSessionFactory().getCurrentSession();
		try {

			String queryString = "from " + model.getName() + " where "
					+ condition;

			Query queryObject = session.createQuery(queryString);

			return queryObject.list();
		} catch (RuntimeException re) {
			// throw re;
			if (session.isConnected() || session.isOpen()) {
				// session.close();
			}
			String queryString = "from " + model.getName() + " where "
					+ condition;
			Query queryObject = session.createQuery(queryString);

			return queryObject.list();
		}
	}

	/**
	 * 根据查询语句 查询
	 *
	 * @param model
	 *            实体
	 * @param con
	 *            查询语句
	 * @param values
	 *            参数
	 *            orderBy
	 * @return
	 */
	public <E> int getCountsByCon(Class<E> model, String con, Object... values) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "select count(id) from " + model.getName() + " where "
				+ con;
		Query query = session.createQuery(hql);
		if (values != null && values.length != 0) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return ((Long) (query.uniqueResult())).intValue();
	}

	/**
	 * 根据查询语句 查询
	 *
	 * @param model
	 *            实体
	 * @param con
	 *            查询语句
	 * @param values
	 *            参数
	 * @param start
	 *            分页
	 * @param end
	 * @param orderBy
	 *            orderBy(如果不需要排序，则传一个空字符串)
	 * @return
	 */
	public <E> List<E> getListByCon(Class<E> model, String con, Integer start,
			Integer end, String orderBy, Object... values) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = " from " + model.getName() + " where " + con + orderBy;
		Query query = session.createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}

		if (start != null) {
			query.setFirstResult(start);
		}
		if (end != null) {
			query.setMaxResults(end);
		}
		return query.list();
	}

	public <E> int getAllCountAndCon(Class<E> model, String condition) {
		Session session = getSessionFactory().getCurrentSession();
		String className = model.getName();
		String queryString = "select count(id) from " + className + " where "
				+ condition;
		Query queryObject = session.createQuery(queryString);
		return Integer.parseInt(queryObject.uniqueResult().toString());
	}

	public <E> List<E> findByPageOrderAndCon(Class<E> model,
			String conditionValue, String orderPropertyName, String value,
			int start, int end) {
		List<E> list = null;
		Session session = getSessionFactory().getCurrentSession();
		try {
			String queryString = "from " + model.getName() + " where "
					+ conditionValue + " order by " + orderPropertyName + " "
					+ value;
			Query queryObject = session.createQuery(queryString);
			queryObject.setFirstResult(start);
			queryObject.setMaxResults(end);
			list = queryObject.list();
		} catch (RuntimeException re) {
			re.printStackTrace();
			if (session.isConnected() || session.isOpen()) {
				session.close();
			}
			String queryString = "from " + model.getName() + " where "
					+ conditionValue + " order by " + orderPropertyName + " "
					+ value;
			System.out.println(queryString);
			Query queryObject = session.createQuery(queryString);
			queryObject.setFirstResult(start);
			queryObject.setMaxResults(end);
			list = queryObject.list();
			// throw re;
		}
		return list;
	}

	public <E> void updatePropertyByIDs(String propertyName, Object value,
			String ids, Class<E> model) {
		try {
			Session session = getSessionFactory().getCurrentSession();
			String className = model.getName();
			String queryString = "update " + className + " set " + propertyName
					+ " = ? where id in(" + ids + ")";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, value);

			queryObject.executeUpdate();
		} catch (RuntimeException re) {

			throw re;
		}
	}

	/**
	 * 根据条件查询总记录数
	 *
	 * @param model
	 *            要查询的实体类
	 * @param keys
	 *            要查询的字段
	 * @param values
	 *            要查询的字段
	 * @return
	 */
	public <E> Object findCountByCustom(Class<E> model, List<String> keys,
			List<Object> values) {
		Session session = getSessionFactory().getCurrentSession();
		StringBuffer sb = new StringBuffer("select count(*) from "
				+ model.getName() + " where 1=1 ");
		if (keys != null && values != null && keys.size() == values.size()) {
			for (int i = 0; i < keys.size(); i++) {
				sb.append(" and " + keys.get(i));
			}
		}
		Query query = session.createQuery(sb.toString());
		if (values != null) {
			for (int i = 0; i < values.size(); i++) {
				query.setParameter(i, values.get(i));
			}
		}
		return query.uniqueResult();
	}

	/**
	 * 根据条件查询分页数据
	 *
	 * @param model
	 *            要查询的实体类
	 * @param keys
	 *            要查询的字段
	 * @param values
	 *            值
	 * @param startIndex
	 *            开始下标
	 * @param pageSize
	 *            页面大小
	 * @param orderby
	 *            排序
	 * @return
	 */
	public <E> List<E> findListByCustom(Class<E> model, List<String> keys,
			List<Object> values, Integer startIndex, Integer pageSize,
			String orderby) {
		Session session = getSessionFactory().getCurrentSession();
		StringBuffer sb = new StringBuffer("from " + model.getName()
				+ " where 1=1 ");
		if (keys != null && values != null && keys.size() == values.size()) {
			for (int i = 0; i < keys.size(); i++) {
				sb.append(" and " + keys.get(i));
			}
		}
		if (orderby != null && !"".equals(orderby)) {
			sb.append(" " + orderby);
		}
		Query query = session.createQuery(sb.toString());
		if (values != null) {
			for (int i = 0; i < values.size(); i++) {
				query.setParameter(i, values.get(i));
			}
		}
		if (startIndex != null) {
			query.setFirstResult(startIndex);
		}
		if (pageSize != null) {
			query.setMaxResults(pageSize);
		}
		return query.list();
	}

	@Override
	public Query createQuery(String oql) {
		return this.sessionFactory.getCurrentSession().createQuery(oql);
	}

	@Override
	public SQLQuery createSqlQuery(String sql) {

		return this.sessionFactory.getCurrentSession().createSQLQuery(sql);
	}

	public <E> List<E> getListBySQL(String sql, Class className,
			Map<String, Object> map, boolean isFuzzy) throws Exception,
			IllegalAccessException {
		Map<Integer, Object> p1 = getSqlPrepareMap(sql, map);
		List<Object> resultLists = getDataList(removeStr(sql, map), p1,className,isFuzzy);
		return (List<E>) resultLists;

	/*	SQLQuery query = createSqlQuery(sql);
		if (map != null) {
			for (String key : map.keySet()) {
				query.setParameter(key, map.get(key));
			}
		}
		if (className == null)
			return query.list();
		List<Object> resultLists = query.list();
		if (resultLists == null)
			return null;
		List<Object> returnList = new ArrayList(resultLists.size());
		Map<Integer, Object> p1 = getSqlPrepareMap(sql, map);
		String[] columns = getColumns(removeStr(sql, map), p1);

		for (Object obj : resultLists) {
			Object returnObj = className.newInstance();
			Object[] temp = null;
			if (columns.length == 1) {
				temp = new Object[] { obj };
			} else {
				temp = (Object[]) obj;
			}
			for (Field field : className.getDeclaredFields()) {
				boolean isValuation = false;
				List<Integer> tempList = null;
				if (isFuzzy) {
					tempList = new ArrayList<Integer>(columns.length);

				}

				for (int i = 0; i < columns.length; i++) {
					// 如果字段属性名，与查询语句的别名相同
					if (removeChar(columns[i]).toUpperCase().equals(
							field.getName().toUpperCase())) {
						isValuation = true;
						field.setAccessible(true);
						field.set(returnObj, temp[i]);
						if (isFuzzy)
							tempList.add(i);
					}
				}
				if (isFuzzy) {
					first: for (int i = 0; i < columns.length; i++) {
						// 如果字段属性名，与查询语句的别名相同
						for (Integer inte : tempList) {
							if (i == inte.intValue())
								continue first;
						}
						if (removeChar(columns[i]).toUpperCase().indexOf(
								field.getName().toUpperCase()) != -1) {
							isValuation = true;
							field.setAccessible(true);
							tempList.add(i);
							try {
								field.set(returnObj, temp[i]);
							} catch (Exception e) {// 由于模糊匹配可能类型不匹配不匹配的将不再转换数据}

							}
						}
					}
				}
			}
			returnList.add(returnObj);
		}

		return (List<E>) returnList;*/
	}

	private String removeChar(String str) {
		if (str == null)
			return null;
		StringBuilder result = new StringBuilder();
		for (char c : str.toCharArray()) {
			if ((48 <= c && c <= 57) || (65 <= c && c <= 90)
					|| (97 <= c && c <= 122))
				result.append(c);
		}
		return result.toString();
	}

	@Override
	public <E> List<E> getListBySQL(String sql, Object objVlaue, boolean isFuzzy)
			throws Exception {
		Class className = objVlaue.getClass();
		Map<Integer, Object> p1 = getSqlPrepareMap(sql, objVlaue);
		List<Object> resultLists = getDataList(removeStr(sql, objVlaue), p1,className,isFuzzy);
		return (List<E>) resultLists;
	}

	private Map<Integer, Object> getSqlPrepareMap(String sql,
			Map<String, Object> map) {
		if (map == null)
			return null;
		Map<Integer, Object> result = new HashMap<Integer, Object>();
		int[] sortArr = new int[map.size()];
		int index = 0;
		for (String key : map.keySet()) {
			result.put(sql.indexOf(":" + key), map.get(key));
			sortArr[index] = sql.indexOf(":" + key);
			index++;
		}
		for (String key : map.keySet()) {
			sql = sql.replace(":" + key, "?");
		}
		for (int i = 0; i < sortArr.length; i++) {

			for (int j = 0; j < sortArr.length - 1 - i; j++) {
				if (sortArr[j] > sortArr[j + 1]) {
					int temp = sortArr[j + 1];
					sortArr[j + 1] = sortArr[j];
					sortArr[j] = temp;
				}
			}

		}
		Map<Integer, Object> returnList = new HashMap<Integer, Object>();
		for (int i = 1; i <= sortArr.length; i++) {
			returnList.put(i, result.get(sortArr[i - 1]));
		}

		return returnList;
	}

	@SuppressWarnings("unused")
	private Map<Integer, Object> getSqlPrepareMap(String sql, Object objVlaue)
			throws Exception {
		if (objVlaue == null)
			return getSqlPrepareMap(sql, null);
		Map<String, Object> map = new HashMap<String, Object>();
		for (Field field : objVlaue.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Object temp = field.get(objVlaue);
			if (temp != null && !"".equals(temp) && field.getModifiers() != 26) {
				map.put(field.getName(), temp);
			}
		}

		return getSqlPrepareMap(sql, map);
	}

	@SuppressWarnings("deprecation")
	private String[] getColumns(String sql, Map<Integer, Object> map)
			throws Exception {
		ConnectionProvider cp = ((SessionFactoryImplementor) sessionFactory)
				.getConnectionProvider();
		Connection connect = cp.getConnection();
		PreparedStatement ps = connect.prepareStatement(sql);
		if (map != null) {
			for (Integer key : map.keySet()) {
				ps.setObject(key, map.get(key));
			}
		}
		ResultSet rs = ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		String[] result = new String[rsmd.getColumnCount()];
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			result[i - 1] = rsmd.getColumnName(i);
		}
		return result;
	}
	@SuppressWarnings("deprecation")
	private <E>List<E> getDataList(String sql, Map<Integer, Object> map,Class className,boolean isFuzzy)
			throws Exception {
		ConnectionProvider cp = ((SessionFactoryImplementor) sessionFactory)
				.getConnectionProvider();
		Connection connect = cp.getConnection();
		PreparedStatement ps = connect.prepareStatement(sql);
		if (map != null) {
			for (Integer key : map.keySet()) {
				ps.setObject(key, map.get(key));
			}
		}
		ResultSet rs = ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		String[] columns = new String[rsmd.getColumnCount()];
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			columns[i - 1] = rsmd.getColumnName(i);
		}
		List<Object[]> resultlists =new ArrayList<Object[]>();
		while(rs.next()){
			Object[] temp = new Object[rsmd.getColumnCount()];
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				temp[i-1] = rs.getObject(i);
			}
			resultlists.add(temp);
		}
		if (className == null)
			return (List<E>) resultlists;
		List<Object> returnList = new ArrayList(resultlists.size());

		for (Object obj : resultlists) {
			Object returnObj = className.newInstance();
			Object[] temp = null;
				temp = (Object[]) obj;
			for (Field field : className.getDeclaredFields()) {
				boolean isValuation = false;
				List<Integer> tempList = null;
				if (isFuzzy) {
					tempList = new ArrayList<Integer>(columns.length);

				}
				for (int i = 0; i < columns.length; i++) {
					// 如果字段属性名，与查询语句的别名相同
					if (removeChar(columns[i]).toUpperCase().equals(
							field.getName().toUpperCase())) {
						isValuation = true;
						field.setAccessible(true);
						field.set(returnObj, temp[i]);
						if (isFuzzy)
							tempList.add(i);
					}
				}
				if (isFuzzy) {
					first: for (int i = 0; i < columns.length; i++) {
						// 如果字段属性名，与查询语句的别名相同
						for (Integer inte : tempList) {
							if (i == inte.intValue())
								continue first;
						}
						if (removeChar(columns[i]).toUpperCase().indexOf(
								field.getName().toUpperCase()) != -1) {
							isValuation = true;
							field.setAccessible(true);
							tempList.add(i);
							try {
								field.set(returnObj, temp[i]);
							} catch (Exception e) {// 由于模糊匹配可能类型不匹配不匹配的将不再转换数据}

							}
						}
					}
				}
			}
			returnList.add(returnObj);
		}

		return (List<E>) returnList;
	}

	@SuppressWarnings("unused")
	private String removeStr(String sql, Map<String, Object> map) {
		if(map==null)return sql;
		for (String key : map.keySet()) {
			sql = sql.replace(":" + key, "?");
		}
		return sql;
	}

	@SuppressWarnings("unused")
	private String removeStr(String sql, Object objVlaue) throws Exception {
		if (objVlaue == null)
			return sql;

		for (Field field : objVlaue.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Object temp = field.get(objVlaue);
			if (temp != null && !"".equals(temp) && field.getModifiers() != 26) {
				sql = sql.replace(":" + field.getName(), "?");
			}
		}
		return sql;
	}

	@Override
	public <E> List<E> getListBySQL(String sql, Class className,
			Map<String, Object> parmentMap) throws Exception {
		// TODO Auto-generated method stub
		return getListBySQL(sql,className,parmentMap,false);
	}

	@Override
	public <E> List<E> getListBySQL(String sql, Object obj) throws Exception {
		// TODO Auto-generated method stub
		return getListBySQL(sql,obj,false);
	}

}
