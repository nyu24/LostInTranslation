package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
//            JPanel countryPanel = new JPanel();
//            JTextField countryField = new JTextField(10);
//            countryField.setText("can");
//            countryField.setEditable(false); // we only support the "can" country code for now
//            countryPanel.add(new JLabel("Country:"));
//            countryPanel.add(countryField);

            JPanel languagePanel = new JPanel();
            //combo box and function
            JComboBox<String> languageComboBox = new JComboBox<>();
            LanguageCodeConverter language = new LanguageCodeConverter();
            List<String> languages =  language.getLanguages();
            languages.sort(null);
            for(String languageName : languages) {
                languageComboBox.addItem(languageName);
            }
            languagePanel.add(languageComboBox);

            JPanel buttonPanel = new JPanel();
            JButton submit = new JButton("Submit");
            buttonPanel.add(submit);

            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            buttonPanel.add(resultLabel);


            // LIST

            LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter("language-codes.txt");
            CountryCodeConverter cCode = new CountryCodeConverter("country-codes.txt");

            JPanel countryNameListPanel = new JPanel();
            countryNameListPanel.setLayout(new BoxLayout(countryNameListPanel, BoxLayout.Y_AXIS));
//            countryNameListPanel.setLayout(new GridLayout(0, 2));
            countryNameListPanel.add(new JLabel(""), 0);

            Translator translator = new JSONTranslator("sample.json");;

            String[] items = new String[translator.getCountryCodes().size()];

            int i = 0;
            for(String getCountryCode : translator.getCountryCodes()) {
                items[i++] = cCode.fromCountryCode(getCountryCode);
            }

            // create the JList with the array of strings and set it to allow multiple
            // items to be selected at once.
            JList<String> list = new JList<>(items);
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);



            // place the JList in a scroll pane so that it is scrollable in the UI
            JScrollPane scrollPane = new JScrollPane(list);
            countryNameListPanel.add(scrollPane, 1);

            /////////////
            final String[] country = {new String()};
            list.addListSelectionListener(new ListSelectionListener() {

                /**
                 * Called whenever the value of the selection changes.
                 *
                 * @param e the event that characterizes the change.
                 */
                @Override
                public void valueChanged(ListSelectionEvent e) {

                    int[] indices = list.getSelectedIndices();
                    String[] items = new String[indices.length];
                    for (int i = 0; i < indices.length; i++) {
                        items[i] = list.getModel().getElementAt(indices[i]);
                    }
                    country[0] = cCode.fromCountry(items[0]);

                }
            });

            // adding listener for when the user clicks the submit button
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //gets the selected lanuage from the combobox and adds it to the languageChosen
                    LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
                    String language = languageCodeConverter.fromLanguage( languageComboBox.getSelectedItem().toString());
                    //String country = cCode.fromCountry(items[0]);

                    // for now, just using our simple translator, but
                    // we'll need to use the real JSON version later.
                    Translator translator = new JSONTranslator();

                    String result = translator.translate(country[0], language);
                    if (result == null) {
                        result = "no translation found!";
                    }
                    resultLabel.setText(result);

                }

            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//            mainPanel.add(countryPanel);
            mainPanel.add(languagePanel);
            mainPanel.add(buttonPanel);
            mainPanel.add(countryNameListPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);


        });
    }
}
