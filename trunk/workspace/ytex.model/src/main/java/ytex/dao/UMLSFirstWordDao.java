package ytex.dao;


import java.util.List;

import ytex.model.UMLSFirstWord;

/**
 * Dao to get Concepts corresponding to the given text
 * @author vijay
 *
 */
public interface UMLSFirstWordDao {

	/**
	 * get Concepts that start with the specified text.
	 * @param fword
	 * @return
	 */
	public abstract List<UMLSFirstWord> getUMLSbyFirstWord(String fword);

}