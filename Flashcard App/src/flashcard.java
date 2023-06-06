public class flashcard {
    String question;
    String answer;
    String category;
    boolean seen;
    Deck deck;

    /* --- CONSTRUCTOR --- */

    //const. with 2 parameters

    public flashcard(String question, String answer){
        this.question = question;
        this.answer = answer;
    }

    public flashcard(String question, String answer, Deck deck){
        this(question,answer);
        this.seen = false;
        this.deck = deck;
    }


    /* --- GETTERS --- */
    /* Getters and setters are important for encapsulation and providing
    controlled access to the properties of an object. */
     /* provide flexibility by allowing you to change the implementation details of the class without
     affecting other parts of the code that use the class.*/
    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public Deck getDeck() {
        return deck;
    }

    public boolean isSeen() {
        return seen;
    }

    /* --- METHODS --- */

    //flip
    public void flip(){
        seen = !seen;
    }

    public void markAsSeen() {
        seen = true;
    }

    public void markAsUnseen() {
        seen = false;
    }

    public void resetCard() {
        seen = false;
        // Additional logic to reset any other properties or states of the flashcard
    }

    @Override
    public String toString() {
        return "Question: " + question + "\nAnswer: " + answer;
    }
}
