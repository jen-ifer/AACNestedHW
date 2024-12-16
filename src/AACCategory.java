import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;

/**
 * Represents the mappings for a single category of items that should
 * be displayed
 * 
 * @author Catie Baker & Jenifer Silva
 *
 */
public class AACCategory implements AACPage {
	String categoryName;
	AssociativeArray<String,String> itemsDisplayed;

	
	/**
	 * Creates a new empty category with the given name
	 * @param name the name of the category
	 */
	public AACCategory(String name) {
		this.categoryName = name;
		this.itemsDisplayed = new AssociativeArray<String,String>();

	}
	
	/**
	 * Adds the image location, text pairing to the category
	 * @param imageLoc the location of the image
	 * @param text the text that image should speak
	 */
	public void addItem(String imageLoc, String text) {
      try {
				this.itemsDisplayed.set(imageLoc, text);
			} catch (NullKeyException e) {
			}

	}

	/**
	 * Returns an array of all the images in the category
	 * @return the array of image locations; if there are no images,
	 * it should return an empty array
	 */
	public String[] getImageLocs() {
	return this.itemsDisplayed.keysAsStrings();
	}

	/**
	 * Returns the name of the category
	 * @return the name of the category
	 */
	public String getCategory() {
		return this.categoryName;
	}

	/**
	 * Returns the text associated with the given image in this category
	 * @param imageLoc the location of the image
	 * @return the text associated with the image
	 * @throws NoSuchElementException if the image provided is not in the current
	 * 		   category
	 */
	public String select(String imageLoc) throws NoSuchElementException {
		try {
      return this.itemsDisplayed.get(imageLoc);
    } catch (KeyNotFoundException e) {
		}
		throw new NoSuchElementException();
	}

	/**
	 * Determines if the provided images is stored in the category
	 * @param imageLoc the location of the category
	 * @return true if it is in the category, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		if(this.itemsDisplayed.hasKey(imageLoc)){
			return true;
		}
		return false;
	}
}
