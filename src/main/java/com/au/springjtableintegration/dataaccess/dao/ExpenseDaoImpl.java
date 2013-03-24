package com.au.springjtableintegration.dataaccess.dao;


import com.au.springjtableintegration.business.model.Expense;
import com.au.springjtableintegration.business.model.ExpenseCategory;
import com.au.springjtableintegration.business.model.ExpenseSubCategory;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ExpenseDaoImpl implements ExpenseDao {

    @Autowired
    protected SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addExpense(Expense expense) {
        sessionFactory.getCurrentSession().save(expense);
    }
    
    @Override
    public void updateExpense(Expense expense) {
        sessionFactory.getCurrentSession().merge(expense);
    }

    @Override
    public List<Expense> listExpenses(int startIndex, int pageSize) {
        return sessionFactory.getCurrentSession().createQuery("from Expense order by date DESC")
                .setFirstResult(startIndex)
                .setMaxResults(pageSize)
                .list();
    }
    
    @Override
    public int getExpenseCount(){
        int count = 0;
        try {
            count = ((Long)sessionFactory.getCurrentSession().createQuery("select count(*) from Expense").uniqueResult()).intValue();
        } catch (Exception e) {
            System.out.println("Exception "+e.getMessage());
        }      
       return count;
    }

    @Override
    public Expense getExpense(Integer id) {
        Expense expense = (Expense) sessionFactory.getCurrentSession().load(Expense.class, id);
        if (expense != null) {
            return expense;
        } else {
            return null;
        }
    }

    @Override
    public void removeExpense(Integer id) {
        Expense expense = (Expense) sessionFactory.getCurrentSession().load(Expense.class, id);
        if (null != expense) {
            sessionFactory.getCurrentSession().delete(expense);
        }
    }

    @Override
    public List<ExpenseCategory> listExpenseCategories() {
        return sessionFactory.getCurrentSession().createQuery("from ExpenseCategory").list();
    }

    @Override
    public ExpenseCategory getExpenseCategory(Integer category) {
        ExpenseCategory expenseCategory = (ExpenseCategory)sessionFactory.getCurrentSession().load(
                ExpenseCategory.class, category);
        return expenseCategory;
    }

    @Override
    public ExpenseSubCategory getExpenseSubCategory(Integer subcategory) {
        ExpenseSubCategory expensesubCategory = (ExpenseSubCategory)sessionFactory.getCurrentSession().load(
                ExpenseSubCategory.class, subcategory);
        return expensesubCategory;
    }

    @Override
    public List<ExpenseSubCategory> listExpenseSubCategories() {
        return sessionFactory.getCurrentSession().createQuery("from ExpenseSubCategory").list();
    }
    
    @Override
    public List<ExpenseSubCategory> getSubCategoriesForCategory(String categoryId){
        return sessionFactory.getCurrentSession().createQuery("from ExpenseSubCategory esc where esc.expenseCategory.id=:categoryId")
               .setParameter("categoryId", new Integer(categoryId))
               .list();
    }
}
