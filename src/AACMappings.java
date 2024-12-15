import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
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
	private static AACCategory homeScreen = new AACCategory("home");



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
		this.current = null;
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
					if (newCategory.equals(homeScreen)) {
						homeScreen = newCategory;
					}
					arrayCat.set(line.substring(0, line.indexOf(" ")), newCategory);
				} else {
					if (newCategory != null) {
						String[] item = line.substring(1).split(" ", 2);
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
		 * @throws KeyNotFoundException 
		 */
		public String select(String imageLoc) throws NoSuchElementException {
		if (current == null) {
			current = homeScreen;
		}
		try {
			if(current != homeScreen && arrayCat.get(imageLoc)== current){
				throw new NoSuchElementException();
			}
		} catch (KeyNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			current = arrayCat.get(imageLoc);
			return "";
		} catch (KeyNotFoundException e) {
			// try {
			// 	return current.select(imageLoc);
			// } catch (NoSuchElementException i) {
			// 	throw new NoSuchElementException();
			// }

		}
		return current.select(imageLoc);
	}

	/**
	 * Provides an array of all the images in the current category
	 * 
	 * @return the array of images in the current category; if there are no images, it should return
	 *         an empty array
	 */
	public String[] getImageLocs() {
		if (current != null && current!=homeScreen)  {
			if(current.getImageLocs().length==0){
				return new String[0];
			}
			return current.getImageLocs();
		}

		// } else if(homeScreen != null && !homeScreen.getCategory().isEmpty() ){
		// 	return homeScreen.getImageLocs();
		// }
		else{
			return arrayCat.keysAsStrings();
		}
	}

	/**
	 * Resets the current category of the AAC back to the default category
	 */
	public void reset() {
		if (current == null || current.equals(homeScreen)) {
			return;
		} else {
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
	public void writeToFile(String filename) throws Exception {
		// PrintWriter pen = new PrintWriter(System.out, true);
		// Writer output = null;
		try {
			FileWriter fw = new FileWriter(filename + ".txt");
			String[] categories = arrayCat.keysAsStrings();
			for (String i : categories) {
				fw.append(i + " " + arrayCat.get(i).getCategory() + "\n");
				String[] items = arrayCat.get(i).getImageLocs();
				for (String j : items) {
					fw.append(">" + j + " " + arrayCat.get(i).select(j) + "\n");
				}
			}

		} catch (IOException e) {
			return;
		}
	}

	/**
	 * Adds the mapping to the current category (or the default category if that is the current
	 * category)
	 * 
	 * @param imageLoc the location of the image
	 * @param text the text associated with the image
	 */
	public void addItem(String imageLoc, String text) {
		if (current == null) {
			current = homeScreen;
		} else {
			current.addItem(imageLoc, text);
		}
	}

	/**
	 * Gets the name of the current category
	 * 
	 * @return returns the current category or the empty string if on the default category
	 */
	public String getCategory() {
		if (current == null) {
			return "";
		} else {
			return current.getCategory();
		}
	}

	/**
	 * Determines if the provided image is in the set of images that can be displayed and false
	 * otherwise
	 * 
	 * @param imageLoc the location of the category
	 * @return true if it is in the set of images that can be displayed, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		if (current != null) {
			return current.hasImage(imageLoc);
		} else {
			return false;
		}
	}
}
