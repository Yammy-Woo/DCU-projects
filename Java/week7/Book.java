enum BookMedium {
    PhysicalBook, 
    EBook, 
    AudioBook
}

enum BookGenre {
    Fiction, 
    NonFiction
}

enum BookRating {
    Rating1,
    Rating2,
    Rating3,
    Rating4,
    Rating5,
}

public class Book {
    private String title;
    private String author;
    private Integer publishedDate;
    private Integer readDate;
    private BookMedium bookMedium;
    private BookGenre bookGenre;
    private BookRating bookRating;
    
    Book(String title, String author, BookGenre bookGenre) {
        this.title = title;
        this.author = author;
        this.bookGenre = bookGenre;
    }

    Book(String title, String author, BookGenre bookGenre, Integer publishedDate) {
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
        this.bookGenre = bookGenre;
    }

    Book(String title, String author, BookGenre bookGenre, Integer publishedDate, Integer readDate, BookMedium bookMedium, BookRating bookRating) {
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
        this.readDate = readDate;
        this.bookMedium = bookMedium;
        this.bookGenre = bookGenre;
        this.bookRating = bookRating;
    }

    public String toString() {
        String output = this.title + " by " + this.author;
        if(this.publishedDate != null) {
            output += " (" + this.publishedDate + ")";
        }
        if(this.readDate != null) {
            int rating = 0;
            switch(this.bookRating) {
                case Rating1:
                    rating = 1;
                    break;
                case Rating2:
                    rating = 2;
                    break;
                case Rating3:
                    rating = 3;
                    break;
                case Rating4:
                    rating = 4;
                    break;
                case Rating5:
                    rating = 5;
                    break;
                default:
                    rating = 0;
            }
            output += " - read in " + this.readDate + ", rated " + rating + "/5";
        }
        return output;
    }

    /* Getters */
    String getTitle() {
        return this.title;
    }

    String getAuthor() {
        return this.author;
    }

    BookGenre getGenre() {
        return this.bookGenre;
    }

    Integer getPublishedDate() {
        return this.publishedDate;
    }

    Integer getReadDate() {
        return this.readDate;
    }

    BookMedium getMedium() {
        return this.bookMedium;
    }

    BookRating getRating() {
        return this.bookRating;
    }

    public static void main(String[] args) {
        Book b1 = new Book("Children of Time", "Adrian Tchaikovsky", BookGenre.Fiction);
        System.out.println(b1);
        Book b2 = new Book("The Fifth Season", "N. K. Jemesin", BookGenre.Fiction, 2015);
        System.out.println(b2);
        Book b3 = new Book("Perdido Street Station", "China Mieville",
            BookGenre.Fiction, 2000, 2020, BookMedium.EBook, BookRating.Rating5);
        System.out.println(b3);

        System.out.println(b1.getTitle());
        System.out.println(b1.getAuthor());
        System.out.println(b1.getGenre());
        System.out.println(b2.getPublishedDate());
        System.out.println(b3.getReadDate());
        System.out.println(b3.getMedium());
        System.out.println(b3.getRating());
    }
}
