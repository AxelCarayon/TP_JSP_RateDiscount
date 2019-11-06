package SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import simplejdbc.DAO;
import simplejdbc.DAOException;

/**
 *
 * @author Axel
 */
public class ExtendedDAO extends DAO{
    
    public ExtendedDAO(DataSource dataSource) {
        super(dataSource);
    }
    
    public List<DiscountEntity> existingDiscountCode() throws DAOException {
		List<DiscountEntity> result = new LinkedList<>();
		String sql = "SELECT * FROM DISCOUNT_CODE";
		try (	Connection connection = myDataSource.getConnection(); 
			Statement stmt = connection.createStatement(); 
			ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				String code = rs.getString("DISCOUNT_CODE");
                                Double rate = rs.getDouble("RATE");
                                result.add(new DiscountEntity(code,rate));
			}
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
		return result;
	}
    
    public int addCustomer(int customerId) throws DAOException {

		// Une requête SQL paramétrée
		String sql = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID = ?";
		try (   Connection connection = myDataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql)
                ) {
                        // Définir la valeur du paramètre
			stmt.setInt(1, customerId);
			
			return stmt.executeUpdate();

		}  catch (SQLException ex) {
			Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
			throw new DAOException(ex.getMessage());
		}
	}
    
    
    /*
    Inutile car contrainte d'intégrité
    
    public List<ProductCodeEntity> existingProductCode() throws DAOException {
		List<ProductCodeEntity> result = new LinkedList<>();
		String sql = "SELECT * FROM PRODUCT_CODE";
		try (	Connection connection = myDataSource.getConnection(); 
			Statement stmt = connection.createStatement(); 
			ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				String discountCode = rs.getString("DISCOUNT_CODE");
                                String prodCode = rs.getString("PROD_CODE");
                                String description = rs.getString("DESCRIPTION");
                                result.add(new ProductCodeEntity(prodCode,discountCode,description));
			}
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
		return result;
	}
    
    
    public boolean discountCodeIsUsed(String c) throws DAOException{
        List<ProductCodeEntity> productCodes = this.existingProductCode();
        List<DiscountEntity> discountCodes = this.existingDiscountCode();
        
        for (DiscountEntity discount : discountCodes){
            String code = discount.getCode();
            for (ProductCodeEntity product : productCodes){
                if (product.getDiscountCode().equals(code)){
                    return true;
                }
            }
        }
        return false;
    }
    */
}
