package models;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MemberDAO {

	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
	private EntityManager em = emf.createEntityManager();
	private List<Member> mem;
	
	public void add(Member m){
		
		m.setAge(m.getAge());
		m.setName(m.getName());
		em.getTransaction().begin();
		em.persist(m);
		em.getTransaction().commit();
		
	}

	
	public boolean delete(String Id){
		try{
			Member a = em.find(Member.class, Integer.parseInt(Id));
			em.getTransaction().begin();
			em.remove(a);
			em.getTransaction().commit();
			return true;
			}
			catch(NullPointerException e){
				return false;
			}
	}

	public List<Member> show(){
		
		mem = em.createQuery("from Member order by id desc", Member.class).setMaxResults(10).getResultList();
		em.clear();
		return mem;
		
	}
	
	public Member showById(String Id){
	
		try{
		return(em.find(Member.class, Integer.parseInt(Id)));
		}
		catch(NullPointerException e){
			return null;
		}
		
	}
	
	public boolean update(Member ab,String Id){
		try{
			Member a = em.find(Member.class,Integer.parseInt(Id));
			a.setAge(ab.getAge());
			a.setName(ab.getName());
			add(a);
			return true;
			}
			catch(NullPointerException e){
				return false;
			}
	}
	
}
