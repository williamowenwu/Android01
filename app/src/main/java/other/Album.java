package other;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents an album in a photo management application. An album has a name, a list of pictures,
 * and a date range that covers all pictures within it. Albums can be navigated picture-by-picture and can
 * be searched based on date ranges or tags associated with the pictures.
 */
public class Album implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * List of pictures contained in the album.
     */
    private ArrayList<Picture> pictures;
    /**
     * The number of pictures in the album. Used for display purposes.
     */
    private int numOfPics; // just used for the table in the user.fxml
    /**
     * The name of the album.
     */
    private String name;
    /**
     * The date range covering all pictures in the album.
     */
    private String dateRange;
    /**
     * The current index of the picture being viewed in the album.
     */
    private int currentIndex; // index for cycling through pictures in slideshow

    /**
     * constructor for Album
     * @param name the name of new Album
     */
    public Album(String name) {
        this.name = name;
        this.pictures = new ArrayList<Picture>();
        this.dateRange = "";
    }

    /**
     * Compares this album with the specified object for equality. Two albums are considered equal if they have the same name.
     *
     * @param o the object to be compared for equality with this album
     * @return true if the specified object is equal to this album
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Album)) return false;
        Album album = (Album) o;
        return Objects.equals(name, album.name);
    }

    // Getters and Setters
    /**
     * Returns the name of the album.
     *
     * @return the name of the album
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the album.
     *
     * @param name the new name for the album
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the number of pictures in the album. This count is updated dynamically as pictures are added or removed.
     *
     * @return the number of pictures in the album
     */
    public int getNumOfPics() {
        return this.pictures.size();
    }

    /**
     * Returns the list of pictures in the album. Modifications to this list will affect the album's content.
     *
     * @return the list of pictures in the album
     */
    public ArrayList<Picture> getPictures(){
        return this.pictures;
    }

    /**
     * Checks if a specific picture is contained within the album.
     *
     * @param picture the picture to check for presence in the album
     * @return true if the picture is contained in the album; false otherwise
     */
    public boolean hasPicture(Picture picture){
        return this.pictures.contains(picture);
    }

    /**
     * Adds a picture to the album and updates the number of pictures and the date range accordingly.
     *
     * @param picture the picture to be added
     */
    public void addPicture(Picture picture) {
        this.pictures.add(picture);
        this.numOfPics++;
        this.updateDateRange();
    }

    /**
     * Removes a picture from the album and updates the number of pictures and the date range accordingly.
     *
     * @param picture the picture to be removed
     */
    public void removePicture(Picture picture) {
        this.pictures.remove(picture);
        this.numOfPics--;
        this.updateDateRange();
    }
    /**
     * Returns the date range covering all pictures in the album. The format is "M/d/y - M/d/y", or a single date "M/d/y" if only one picture is present.
     *
     * @return the date range of the album
     */
    public String getDateRange() {
        return dateRange;
    }

    /**
     * Sets the date range of the album. This is usually updated automatically as pictures are added or removed.
     *
     * @param dateRange the new date range of the album
     */
    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    /**
     * Returns the current index of the picture being viewed in the album.
     *
     * @return the current index
     */
    public int getCurrentIndex(){
        return this.currentIndex;
    }

    /**
     * Sets the current index to the next picture in the album, if any. This method is useful for navigating pictures in a slideshow manner.
     */
    public void nextPicture(){
        this.currentIndex++;
    }
    /**
     * Sets the current picture index based on a selected picture. If the picture exists in the album, the index is updated to match the picture's index.
     *
     * @param selectedPicture the picture to set as the current index
     */
    public void setCurrentIndexFromPicture(Picture selectedPicture) {
        int index = this.pictures.indexOf(selectedPicture);
        if (index != -1) {
            this.currentIndex = index;
        }
    }
    /**
     * Sets the current index to the previous picture in the album, if any. This method supports reverse navigation in a slideshow.
     */
    public void previousPicture(){
        this.currentIndex--;
    }
    /**
     * Sets the current index to a new value. This method directly updates the index used for navigating pictures in the album.
     *
     * @param newIndex the new index to set as the current picture index
     */
    public void setCurrentIndex(int newIndex){
        this.currentIndex = newIndex;
    }

    /**
     * Updates the date range of the album to span from the earliest to the latest date among the contained pictures.
     */
    public void updateDateRange() {
        if (pictures.isEmpty()) {
            this.dateRange = "";
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("M/d/y");

        // Find the earliest and latest dates among the pictures
        Calendar earliest = pictures.stream()
                .min(Comparator.comparing(Picture::getPhotoDate))
                .map(Picture::getPhotoDate)
                .orElse(Calendar.getInstance()); // Fallback, should not happen if pictures is not empty
        Calendar latest = pictures.stream()
                .max(Comparator.comparing(Picture::getPhotoDate))
                .map(Picture::getPhotoDate)
                .orElse(Calendar.getInstance()); // Fallback, should not happen if pictures is not empty
        //If only one picture, this will show the same date twice, which might be fine.
        if (earliest.equals(latest)) {
            this.dateRange = sdf.format(earliest.getTime());
        } else {
            this.dateRange = sdf.format(earliest.getTime()) + " - " + sdf.format(latest.getTime());
        }
    }

    //* SEARCHING */

    /**
     * Searches for photos within the album that were taken between the specified start and end dates.
     *
     * @param startDate the start date of the search range
     * @param endDate   the end date of the search range
     * @return a list of pictures taken within the specified date range
     */
    public List<Picture> searchPhotosByDateRange(Calendar startDate, Calendar endDate) {
        return pictures.stream()
                .filter(picture -> !picture.getPhotoDate().before(startDate) && !picture.getPhotoDate().after(endDate))
                .collect(Collectors.toList());
    }

    /**
     * Searches for photos in the album by a single tag-value pair. Finds pictures that have a tag matching
     * the specified tag type and value.
     *
     * @param tagType the type of the tag to search for
     * @param tagValue the value of the tag to match
     * @return a list of pictures that match the specified tag criteria
     */
  /*  public List<Picture> searchPhotosByTag(String tagType, String tagValue) {
        return pictures.stream()
                .filter(picture -> picture.getAllTags().stream()
                        .anyMatch(tag -> tag.getType().equalsIgnoreCase(tagType) && tag.getValues().contains(tagValue)))
                .collect(Collectors.toList());
    }
*/
    /**
     * Searches for photos in the album that match both of two specified tag-value pairs.
     * This method applies an AND logic to the search criteria.
     *
     * @param tag1Key the type of the first tag
     * @param tag1Value the value of the first tag
     * @param tag2Key the type of the second tag
     * @param tag2Value the value of the second tag
     * @return a list of pictures that match both specified tag criteria
     */
   /* public List<Picture> searchPhotosByTagsAnd(String tag1Key, String tag1Value, String tag2Key, String tag2Value) {
        return pictures.stream()
                .filter(picture ->
                        picture.getAllTags().stream().anyMatch(tag ->
                                tag.getType().equalsIgnoreCase(tag1Key) && tag.getValue().equals(tag1Value))
                                &&
                                picture.getAllTags().stream().anyMatch(tag ->
                                        tag.getType().equalsIgnoreCase(tag2Key) && tag.getValue().equals(tag2Value))
                )
                .collect(Collectors.toList());
    } */

    /**
     * Searches for photos in the album that match either of two specified tag-value pairs.
     * This method applies an OR logic to the search criteria.
     *
     * @param tagType1 the type of the first tag
     * @param tagValue1 the value of the first tag
     * @param tagType2 the type of the second tag
     * @param tagValue2 the value of the second tag
     * @return a list of pictures that match either of the specified tag criteria
     */
 /*   public List<Picture> searchPhotosByTagsOr(String tagType1, String tagValue1, String tagType2, String tagValue2) {
        return pictures.stream()
                .filter(picture -> picture.getAllTags().stream().anyMatch(tag -> tag.getType().equalsIgnoreCase(tagType1) && tag.getValues().contains(tagValue1))
                        || picture.getAllTags().stream().anyMatch(tag -> tag.getType().equalsIgnoreCase(tagType2) && tag.getValues().contains(tagValue2)))
                .collect(Collectors.toList());
    }*/
    /**
     * Creates a new album from a list of search results. This method allows for the creation of sub-albums
     * based on search criteria applied to the main album.
     *
     * @param albumName the name of the new album to be created
     * @param searchResults a list of pictures that are the result of a search operation
     * @return an Album object containing the pictures from the search results
     */
    public Album createAlbumFromSearchResults(String albumName, List<Picture> searchResults) {
        Album newAlbum = new Album(albumName);
        searchResults.forEach(newAlbum::addPicture);
        return newAlbum;
    }

}


