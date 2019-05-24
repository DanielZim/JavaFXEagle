package model;

import model.sceneObjects.coordinateTransformables.EaglePackage;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Library.
 */
public class Library {

    // required
    private String name;
    private String description;

    // optional
    private Map<String, EaglePackage> packages;
    private boolean isSetPackages;

    /**
     * Instantiates a new Library.
     *
     * @param name        the name
     * @param description the description
     */
    public Library(String name, String description) {
        this.name = name;
        this.description = description;

        this.packages = new HashMap<>();
        this.isSetPackages = false;
    }

    /**
     * Add package.
     *
     * @param eaglePackage the eagle package
     */
    public void addPackage(EaglePackage eaglePackage) {
        packages.put(eaglePackage.getName(), eaglePackage);
        this.isSetPackages = true;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets packages.
     *
     * @return the packages
     */
    public Map<String, EaglePackage> getPackages() {
        return packages;
    }

    /**
     * Gets is set packages.
     *
     * @return the is set packages
     */
    public boolean getIsSetPackages() {
        return isSetPackages;
    }
}
