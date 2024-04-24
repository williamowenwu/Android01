package other;

import java.io.Serializable;

public class PhotoItem implements Serializable {
    private String imagePath;

    public PhotoItem(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
