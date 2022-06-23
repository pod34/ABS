package DTOs;

import java.io.Serializable;
import java.util.List;

public class CategoriesDTO implements Serializable {
    List<String> categories;

    public CategoriesDTO(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        int i = 1;
        StringBuilder allCategories = new StringBuilder();

        for (String curCategory : categories) {
            allCategories.append(i + ")" + curCategory + "\n");
            i++;
        }
        return allCategories.toString();

    }
}

