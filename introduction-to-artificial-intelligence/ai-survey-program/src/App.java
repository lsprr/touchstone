import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class App {

    // A scanner utilized for capturing input from the console.
    private static final Scanner scanner = new Scanner(System.in);
    // Array for holding survey queries
    private static final List<Question> questions = new ArrayList<>();
    // Associates political factions with their respective response file identifiers
    private static final Map<String, String> partyToFileMap = new HashMap<>();
    // Links response choices with their significance score for each political group
    private static final Map<String, Map<String, Integer>> answerWeightMap = new HashMap<>();

    public static void main(String[] args) {
        // Sets up the questionnaire and establishes the mappings
        initializeQuestionsAndMapping();
        // Executes the survey and accumulates answers
        List<String> userResponses = conductSurvey();
        // Concludes political affiliation from the collected answers
        String predictedParty = predictPoliticalParty(userResponses);
        System.out.println("Based on your answers, we predict your political affiliation is: " + predictedParty);
        // Attempts to record responses in a file, managing possible input/output errors
        try {
            saveResponsesToFile(userResponses, predictedParty);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // Sets up the questionnaire, along with file name and answer weight
    // associations
    private static void initializeQuestionsAndMapping() {
        String[] questionStrings = new String[] {
                "What should the government do to help the poor?\n A. Expand social welfare programs.\n B. Implement tax cuts and create jobs.\n C. Promote sustainable community programs.\n D. Reduce government involvement in welfare.",
                "What is your stance on environmental policy?\n A. Strongly support environmental regulations.\n B. Support government-led environmental initiatives.\n C. Favor market-based environmental solutions.\n D. Oppose government-imposed environmental regulations.",
                "How should the education system be managed?\n A. Increase public education funding.\n B. Support school choice and vouchers.\n C. Focus on environmental and sustainability education.\n D. Minimize government control and promote private schooling.",
                "What is your view on healthcare?\n A. Advocate for a single-payer healthcare system.\n B. Support private healthcare and market competition.\n C. Emphasize preventive care and holistic approaches.\n D. Oppose government involvement in healthcare.",
                "What should be the government's role in the economy?\n A. Implement progressive taxation and regulate industries.\n B. Support free market policies and lower taxes.\n C. Promote green jobs and sustainable economic practices.\n D. Reduce government intervention in the economy.",
                "What is your stance on gun control?\n A. Support strict gun control laws.\n B. Defend the right to bear arms with minimal restrictions.\n C. Focus on community safety and conflict resolution.\n D. Oppose most forms of gun regulation.",
                "How should immigration be handled?\n A. Advocate for open and compassionate immigration policies.\n B. Prioritize secure borders and immigration reform.\n C. Promote diversity and global responsibility.\n D. Support individual freedom of movement with minimal restrictions.",
                "What is your view on foreign policy?\n A. Support diplomatic solutions and international alliances.\n B. Prioritize national security and military strength.\n C. Advocate for peace and global environmental cooperation.\n D. Promote non-interventionism and defense only.",
                "What is your stance on taxation?\n A. Support progressive taxation to fund social programs.\n B. Advocate for tax cuts and reducing government spending.\n C. Propose eco-taxes to support environmental initiatives.\n D. Favor minimal taxation and maximum financial freedom.",
                "How should the government address climate change?\n A. Implement aggressive policies to combat climate change.\n B. Encourage market-based solutions and innovation.\n C. Prioritize environmental sustainability and green technologies.\n D. Limit government intervention in climate-related issues."
        };

        // Analyzes each question string, dividing it into the main text and possible
        // choices
        for (String q : questionStrings) {
            String[] parts = q.split("\n");
            String questionText = parts[0];
            String[] options = Arrays.copyOfRange(parts, 1, parts.length);
            questions.add(new Question(questionText, options));
        }

        // Establishes file name correspondences for each political faction
        partyToFileMap.put("Democratic Party", "democratic_responses.txt");
        partyToFileMap.put("Republican Party", "republican_responses.txt");
        partyToFileMap.put("Green Party", "green_responses.txt");
        partyToFileMap.put("Libertarian Party", "libertarian_responses.txt");

        // Creates the mappings for the weights of various answers
        initializeAnswerWeightMapping();
    }

    // Sets up the association of answer values with respective political groups
    private static void initializeAnswerWeightMapping() {
        Map<String, Integer> weightsForA = new HashMap<>();
        weightsForA.put("Democratic Party", 3);
        weightsForA.put("Republican Party", 1);
        weightsForA.put("Green Party", 2);
        weightsForA.put("Libertarian Party", 1);
        answerWeightMap.put("A", weightsForA);

        Map<String, Integer> weightsForB = new HashMap<>();
        weightsForB.put("Democratic Party", 1);
        weightsForB.put("Republican Party", 3);
        weightsForB.put("Green Party", 1);
        weightsForB.put("Libertarian Party", 2);
        answerWeightMap.put("B", weightsForB);

        Map<String, Integer> weightsForC = new HashMap<>();
        weightsForC.put("Democratic Party", 2);
        weightsForC.put("Republican Party", 1);
        weightsForC.put("Green Party", 3);
        weightsForC.put("Libertarian Party", 1);
        answerWeightMap.put("C", weightsForC);

        Map<String, Integer> weightsForD = new HashMap<>();
        weightsForD.put("Democratic Party", 1);
        weightsForD.put("Republican Party", 2);
        weightsForD.put("Green Party", 1);
        weightsForD.put("Libertarian Party", 3);
        answerWeightMap.put("D", weightsForD);
    }

    // Executes the survey, presenting queries and gathering participant answers
    private static List<String> conductSurvey() {
        List<String> responses = new ArrayList<>();
        // Loop through each individual question
        for (Question question : questions) {
            System.out.println(question.getQuestionText());
            for (String option : question.getOptions()) {
                System.out.println(option);
            }
            // Show the question and its corresponding choices
            System.out.print("Your answer: ");
            // Obtain a verified response from the participant
            String answer = getValidatedAnswer();
            responses.add(answer);
        }

        System.out.println("Which political party do you currently identify with?");
        String selfIdentifiedParty = scanner.nextLine().trim();
        responses.add(selfIdentifiedParty);

        return responses;
    }

    // Confirms the legitimacy of participant responses for the survey
    private static String getValidatedAnswer() {
        String answer = scanner.nextLine().trim().toUpperCase();
        while (!answer.matches("[ABCD]")) {
            System.out.print("Please enter a valid answer (A, B, C, or D): ");
            answer = scanner.nextLine().trim().toUpperCase();
        }
        return answer;
    }

    // Concludes political affiliation from the answers provided in the survey
    private static String predictPoliticalParty(List<String> responses) {
        Map<String, Integer> partyScore = new HashMap<>();
        // Tally scores for each political group using the survey responses
        for (String response : responses) {
            answerWeightMap.getOrDefault(response, new HashMap<>()).forEach(
                    (party, weight) -> partyScore.put(party, partyScore.getOrDefault(party, 0) + weight));
        }
        return Collections.max(partyScore.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    // Archives the responses in a file, sorted by the estimated political party
    private static void saveResponsesToFile(List<String> responses, String party) throws IOException {
        String filename = partyToFileMap.getOrDefault(party, "default.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            for (String response : responses) {
                writer.write(response + "\n");
            }
            writer.write("\n");
        }
    }

    // Nested class for encapsulating a survey question along with its choices
    static class Question {
        private String questionText;
        private List<String> options;

        public Question(String questionText, String... options) {
            this.questionText = questionText;
            this.options = Arrays.asList(options);
        }

        public String getQuestionText() {
            return questionText;
        }

        public List<String> getOptions() {
            return options;
        }
    }
}
