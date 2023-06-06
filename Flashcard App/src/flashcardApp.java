/*import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class flashcardApp extends JFrame {
    private flashcard currentFlashcard;
    private JLabel questionLabel;
    private JLabel answerLabel;
    private JButton flipButton;

    public flashcardApp() {
        // Set up the JFrame
        setTitle("Flashcard App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLayout(new BorderLayout());

        // Create the flashcard
        currentFlashcard = new flashcard("What is the capital of France?", "Paris");

        // Create the UI components
        questionLabel = new JLabel(currentFlashcard.getQuestion());
        answerLabel = new JLabel(currentFlashcard.getAnswer());
        answerLabel.setVisible(false);

        flipButton = new JButton("Flip");
        flipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flipFlashcard();
            }
        });

        // Add the components to the JFrame
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.add(questionLabel);
        centerPanel.add(answerLabel);

        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.add(flipButton);

        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void flipFlashcard() {
        currentFlashcard.flip();

        if (currentFlashcard.isSeen()) {
            questionLabel.setVisible(false);
            answerLabel.setVisible(true);
        } else {
            questionLabel.setVisible(true);
            answerLabel.setVisible(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                flashcardApp app = new flashcardApp();
                app.setVisible(true);
            }
        });
    }
}*/


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class flashcardApp extends JFrame {
    private DeckList deckList;
    private DefaultListModel<String> deckListModel;
    private JList<String> deckListUI;

    public flashcardApp() {
        // Set up the JFrame
        setTitle("Flashcard App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        // Create a deck list
        deckList = new DeckList();

        // Create sample decks and flashcards
        Deck deck1 = new Deck("Deck 1");
        Deck deck2 = new Deck("Deck 2");

        flashcard flashcard1 = new flashcard("Question 1", "Answer 1", deck1);
        flashcard flashcard2 = new flashcard("Question 2", "Answer 2", deck1);
        flashcard flashcard3 = new flashcard("Question 3", "Answer 3", deck2);

        deck1.addFlashcards(flashcard1);
        deck1.addFlashcards(flashcard2);
        deck2.addFlashcards(flashcard3);

        deckList.addDeck(deck1);
        deckList.addDeck(deck2);

        // Create the UI components
        deckListModel = new DefaultListModel<>();
        for (Deck deck : deckList.getDecks()) {
            deckListModel.addElement(deck.getTitle());
        }

        deckListUI = new JList<>(deckListModel);
        deckListUI.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane deckListScrollPane = new JScrollPane(deckListUI);

        // Add the components to the JFrame
        add(deckListScrollPane, BorderLayout.CENTER);

        // Add an action listener to handle deck selection
        deckListUI.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = deckListUI.getSelectedIndex();
                if (selectedIndex != -1) {
                    Deck selectedDeck = deckList.getDecks().get(selectedIndex);
                    displayFlashcards(selectedDeck);
                }
            }
        });
    }

    private void displayFlashcards(Deck deck) {
        // Clear the current content
        getContentPane().removeAll();

        // Create the UI components for displaying flashcards
        DefaultListModel<String> flashcardListModel = new DefaultListModel<>();
        for (flashcard flashcard : deck.getFlashcards()) {
            flashcardListModel.addElement(flashcard.getQuestion());
        }

        JList<String> flashcardList = new JList<>(flashcardListModel);
        flashcardList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane flashcardListScrollPane = new JScrollPane(flashcardList);

        add(flashcardListScrollPane, BorderLayout.CENTER);

        // Add an action listener to handle flashcard selection
        flashcardList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = flashcardList.getSelectedIndex();
                if (selectedIndex != -1) {
                    flashcard selectedFlashcard = deck.getFlashcards().get(selectedIndex);
                    displayFlashcard(selectedFlashcard);
                }
            }
        });

        // Repaint the UI
        revalidate();
        repaint();
    }

    private void displayFlashcard(flashcard flashcard) {
        // Clear the current content
        getContentPane().removeAll();

        // Create the UI components for displaying the flashcard
        JLabel questionLabel = new JLabel(flashcard.getQuestion());
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel answerLabel = new JLabel(flashcard.getAnswer());
        answerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        answerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        answerLabel.setVisible(false);

        JButton flipButton = new JButton("Flip");
        flipButton.setFont(new Font("Arial", Font.BOLD, 14));
        flipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flipFlashcard(answerLabel);
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(questionLabel, BorderLayout.CENTER);
        centerPanel.add(answerLabel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        southPanel.setBackground(Color.WHITE);
        southPanel.add(flipButton);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(southPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);

        // Repaint the UI
        revalidate();
        repaint();
    }

    private void flipFlashcard(JLabel answerLabel) {
        answerLabel.setVisible(!answerLabel.isVisible());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                flashcardApp app = new flashcardApp();
                app.setVisible(true);
            }
        });
    }
}
