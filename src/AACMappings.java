import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.NoSuchElementException;

/**
 * Creates a set of mappings of an AAC that has two levels, one for categories and then within each
 * category, it has images that have associated text to be spoken. This class provides the methods
 * for interacting with the categories and updating the set of images that would be shown and
 * handling an interactions.
 * 
 * @author Catie Baker & Jenifer Silva
 *
 */
public class AACMappings implements AACPage {

	String nameF;
	AssociativeArray<String, AACCategory> arrayCat;
	AssociativeArray<String, String> itemsDisplayed;
	AACCategory current;
	private static AACCategory homeScreen;



	/**
	 * Creates a set of mappings for the AAC based on the provided file. The file is read in to create
	 * categories and fill each of the categories with initial items. The file is formatted as the
	 * text location of the category followed by the text name of the category and then one line per
	 * item in the category that starts with > and then has the file name and text of that image
	 * 
	 * for instance: img/food/plate.png food >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing and food has french fries and
	 * watermelon and clothing has a collared shirt
	 * 
	 * @param filename the name of the file that stores the mapping information
	 */
	public AACMappings(String filename) throws IOException {
		this.nameF = filename;
		this.arrayCat = new AssociativeArray<String, AACCategory>();
		AACCategory newCategory = new AACCategory("");

		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = "";
			while ((line = br.readLine()) != null) {
				if (!line.contains(" ")) {// check for valid line
					continue;
				}
				int index = line.indexOf(' ');
				if (!line.startsWith(">")) {
					newCategory = new AACCategory(line.substring(line.indexOf(" ") + 1));
					arrayCat.set(line.substring(0, line.indexOf(" ")), newCategory);
				} else {
					if (newCategory != null) {
						String[] item = line.substring(1).split("", 2);
						newCategory.addItem(item[0], item[1]);
					}
				}
			}
		} catch (FileNotFoundException e) {
			return;
		} catch (NullKeyException e) {
			return;
		}
	}

	/**
	 * Given the image location selected, it determines the action to be taken. This can be updating
	 * the information that should be displayed or returning text to be spoken. If the image provided
	 * is a category, it updates the AAC's current category to be the category associated with that
	 * image and returns the empty string. If the AAC is currently in a category and the image
	 * provided is in that category, it returns the text to be spoken.
	 * 
	 * @param imageLoc the location where the image is stored
	 * @return if there is text to be spoken, it returns that information, otherwise it returns the
	 *         empty string
	 * @throws NoSuchElementException if the image provided is not in the current category
	 */
	public String select(String imageLoc) {
		if (current != null){
			if(current.equals(homeScreen)){
				try {
					current = arrayCat.get(imageLoc);
					return "";
				} catch (KeyNotFoundException e) {
					throw new NoSuchElementException();
				}
			}
			return current.select(imageLoc);
		}else{
			throw new NoSuchElementException();
		}
	}

	/**
	 * Provides an array of all the images in the current category
	 * 
	 * @return the array of images in the current category; if there are no images, it should return
	 *         an empty array
	 */
	public String[] getImageLocs() {
		if(current != null){
			return current.getImageLocs();
		}
		else{
			return new String [0];
		}
	}

	/**
	 * Resets the current category of the AAC back to the default category
	 */
	public void reset() {
		if(current.equals(homeScreen)){
			return;
		}
		else{
			current = homeScreen;
		}

	}


	/**
	 * Writes the ACC mappings stored to a file. The file is formatted as the text location of the
	 * category followed by the text name of the category and then one line per item in the category
	 * that starts with > and then has the file name and text of that image
	 * 
	 * for instance: img/food/plate.png food >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing and food has french fries and
	 * watermelon and clothing has a collared shirt
	 * 
	 * @param filename the name of the file to write the AAC mapping to
	 */
	public void writeToFile(String filename) {

	}null

	/**
	 * Adds the mapping to the current category (or the default category if that is the current
	 * category)
	 * 
	 * @param imageLoc the location of the image
	 * @param text the text associated with the image
	 */
	public void addItem(String imageLoc, String text) {
		if(current == null){
			return;
		}
		else{
			current.addItem(imageLoc, text);
		}
	}


	/**
	 * Gets the name of the current category
	 * 
	 * @return returns the current category or the empty string if on the default category
	 */
	public String getCategory() {
		if(current == null){
			return "";
		}
		else{
			return current.getCategory();
		}
		return null;
	}


	/**
	 * Determines if the provided image is in the set of images that can be displayed and false
	 * otherwise
	 * 
	 * @param imageLoc the location of the category
	 * @return true if it is in the set of images that can be displayed, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		if(current != null){
			return current.hasImage(imageLoc);
		}
		else{
			return false;
		}
	}
}
