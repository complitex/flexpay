package org.flexpay.eirc.test;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.Town;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.Service;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.Random;

public class RandomObjects {

	private HibernateTemplate hibernateTemplate;
	private Random rand;

	public RandomObjects(Random rand, ApplicationContext context) {
		this.rand = rand;
		hibernateTemplate = (HibernateTemplate) context.getBean("hibernateTemplate");
	}

	public Apartment getRandomApartment(final Town town) {
		return (Apartment) hibernateTemplate.execute(new HibernateCallback<Object>() {
            @Override
			public Object doInHibernate(Session session) throws HibernateException {
				String query = "select count(*) " +
							   "from Apartment a inner join a.building b left join b.buildingses bs inner join bs.street s " +
							   "where s.parent.id=?";
				Long count = (Long) session.createQuery(query)
						.setLong(0, town.getId()).uniqueResult();
				if (count == null) {
					throw new RuntimeException("No apartments in town #" + town.getId());
				}
				int randInt = Math.abs(rand.nextInt());
				int next = randInt % count.intValue();

				query = "select a.id " +
						"from Apartment a inner join a.building b left join b.buildingses bs inner join bs.street s " +
						"where s.parent.id=?";
				Long id = (Long) session.createQuery(query)
						.setLong(0, town.getId()).setFirstResult(next).setMaxResults(1).uniqueResult();

				return session.getNamedQuery("Apartment.readFull").setLong(0, id).uniqueResult();
			}
		});
	}

	public Person getRandomPerson() {
		return (Person) hibernateTemplate.execute(new HibernateCallback<Object>() {
            @Override
			public Object doInHibernate(Session session) throws HibernateException {
				String query = "select max(id) from Person where status=0";
				Long maxId = (Long) session.createQuery(query).uniqueResult();
				long randLong = Math.abs(rand.nextLong());
				long id = (randLong % maxId) + 1;

				return session.getNamedQuery("Person.readFull").setLong(0, id).uniqueResult();
			}
		});
	}

	public ServiceProvider getRandomServiceProvider() {
		return (ServiceProvider) hibernateTemplate.execute(new HibernateCallback<Object>() {
            @Override
			public Object doInHibernate(Session session) throws HibernateException {
				String query = "select max(id) from ServiceProvider";
				Long maxId = (Long) session.createQuery(query).uniqueResult();
				long randLong = Math.abs(rand.nextLong());
				long id = (randLong % maxId) + 1;

				return session.load(ServiceProvider.class, id);
			}
		});
	}

	public Service getRandomService(final ServiceProvider provider) {
		return (Service) hibernateTemplate.execute(new HibernateCallback<Object>() {
            @Override
			public Object doInHibernate(Session session) throws HibernateException {
				String query = "select count(id) from Service where serviceProvider.id=?";
				Long count = (Long) session.createQuery(query)
						.setLong(0, provider.getId()).uniqueResult();
				if (count == 0) {
					throw new RuntimeException("No services for provider #" + provider.getId());
				}
				int randInt = Math.abs(rand.nextInt());
				int first = randInt % count.intValue();

				return session.createQuery("select distinct s from Service s left join fetch s.serviceType where s.serviceProvider.id=?")
						.setLong(0, provider.getId()).setFirstResult(first).setMaxResults(1).uniqueResult();
			}
		});
	}
}
