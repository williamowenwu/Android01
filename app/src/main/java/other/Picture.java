package other;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * Represents a picture in a photo management application. Each picture has a unique name within its album,
 * a file path to the stored image, a caption, a date the photo was taken, and a list of applied tags.
 */
public class Picture implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * The unique name of the picture within an album.
     */
    private String name; // Unique identifier within an album
    /**
     * The file path to the image.
     */
    private String filePath;
    /**
     * A caption for the picture.
     */
    private String caption;
    /**
     * The date the picture was taken.
     */
    private Calendar photoDate;
    /**
     * A list of tags applied to the picture.
     */
 //   private List<Tag> appliedTags;

    /**
     * Constructs a new Picture with the specified name, file path, and photo date. Initializes the caption as empty
     * and creates an empty list for tags.
     *
     * @param name the unique name of the picture
     * @param filePath the file path where the picture is stored
     * @param photoDate the date the picture was taken
     */
    public Picture(String name, String filePath, Calendar photoDate) {
        this.name = name;
        this.filePath = filePath;
        this.photoDate = photoDate;
        this.caption = "";
    //    this.appliedTags = new ArrayList<Tag>();
    }

    /**
     * Indicates whether some other object is "equal to" this one by comparing names, photo dates, applied tags,
     * captions, and file paths.
     *
     * @param o the reference object with which to compare
     * @return true if this object is the same as the argument; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Picture)) return false;
        Picture picture = (Picture) o;
        return Objects.equals(name, picture.getName()) &&
                Objects.equals(photoDate, picture.getPhotoDate()) &&
         //       Objects.equals(appliedTags, picture.getAllTags()) &&
                Objects.equals(caption, picture.getCaption()) &&
                Objects.equals(filePath, picture.getFilePath());
    }

    /**
     * Returns the name of the picture.
     *
     * @return the name of the picture
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the picture.
     *
     * @param name the new name for the picture
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the file path of the picture.
     *
     * @return the file path where the picture is stored
     */
    public String getFilePath(){
        return this.filePath;
    }

    /**
     * Returns the caption of the picture.
     *
     * @return the caption of the picture
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Sets the caption for the picture.
     *
     * @param caption the new caption for the picture
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Returns the date the picture was taken.
     *
     * @return the date the picture was taken
     */
    public Calendar getPhotoDate() {
        return photoDate;
    }

    /**
     * Checks if a specific tag is applied to the picture.
     *
     * @param tag the tag to check for
     * @return true if the tag is applied; false otherwise
     */
 /*   public boolean hasApplied(Tag tag){
        return this.appliedTags.contains(tag);
    }

    /**
     * Adds a tag to the list of tags applied to the picture.
     *
     * @param tag the tag to add

    public void addTag(Tag tag){
        this.appliedTags.add(tag);
    }

    /**
     * Returns a list of all tags applied to the picture.
     *
     * @return a list of applied tags

    public List<Tag> getAllTags(){
        return this.appliedTags;
    }

    /**
     * Removes a tag from the picture based on the tag type.
     *
     * @param tag the type of tag to remove
     * @return true if the tag was successfully removed; false otherwise

    public boolean removeTag(String tag){
        for(Tag appliedTag: appliedTags){
            if(appliedTag.getType().equalsIgnoreCase(tag)){
                return this.appliedTags.remove(appliedTag);
            }
        }
        return false;
    }
*/
}
