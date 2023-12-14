import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final List<String> questions = Arrays.asList(
            "What should the government do to help the poor?\n A. Expand social welfare programs.\n B. Implement tax cuts and create jobs.\n C. Promote sustainable community programs.\n D. Reduce government involvement in welfare.",
            "What is your stance on environmental policy?\n A. Strongly support environmental regulations.\n B. Support government-led environmental initiatives.\n C. Favor market-based environmental solutions.\n D. Oppose government-imposed environmental regulations.",
            "How should the education system be managed?\n A. Increase public education funding.\n B. Support school choice and vouchers.\n C. Focus on environmental and sustainability education.\n D. Minimize government control and promote private schooling.",
            "What is your view on healthcare?\n A. Advocate for a single-payer healthcare system.\n B. Support private healthcare and market competition.\n C. Emphasize preventive care and holistic approaches.\n D. Oppose government involvement in healthcare.",
            "What should be the government's role in the economy?\n A. Implement progressive taxation and regulate industries.\n B. Support free market policies and lower taxes.\n C. Promote green jobs and sustainable economic practices.\n D. Reduce government intervention in the economy.",
            "What is your stance on gun control?\n A. Support strict gun control laws.\n B. Defend the right to bear arms with minimal restrictions.\n C. Focus on community safety and conflict resolution.\n D. Oppose most forms of gun regulation.",
            "How should immigration be handled?\n A. Advocate for open and compassionate immigration policies.\n B. Prioritize secure borders and immigration reform.\n C. Promote diversity and global responsibility.\n D. Support individual freedom of movement with minimal restrictions.",
            "What is your view on foreign policy?\n A. Support diplomatic solutions and international alliances.\n B. Prioritize national security and military strength.\n C. Advocate for peace and global environmental cooperation.\n D. Promote non-interventionism and defense only.",
            "What is your stance on taxation?\n A. Support progressive taxation to fund social programs.\n B. Advocate for tax cuts and reducing government spending.\n C. Propose eco-taxes to support environmental initiatives.\n D. Favor minimal taxation and maximum financial freedom.",
            "How should the government address climate change?\n A. Implement aggressive policies to combat climate change.\n B. Encourage market-based solutions and innovation.\n C. Prioritize environmental sustainability and green technologies.\n D. Limit government intervention in climate-related issues.");

    private static final Map<String, String> answerToPartyMap = new HashMap<>();
    private static final String DEMOCRATIC_FILE = "democratic_responses.txt";
    private static final String REPUBLICAN_FILE = "republican_responses.txt";
    private static final String GREEN_FILE = "green_responses.txt";
    private static final String LIBERTARIAN_FILE = "libertarian_responses.txt";

    public static void main(String[] args) {
        initializeAnswerMapping();
        List<String> userResponses = conductSurvey();
        String predictedParty = predictPoliticalParty(userResponses);
        System.out.println("Based on your answers, we predict your political affiliation is: " + predictedParty);
        try {
            saveResponsesToFile(userResponses, predictedParty);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void initializeAnswerMapping() {
        answerToPartyMap.put("A", "Democratic Party");
        answerToPartyMap.put("B", "Republican Party");
        answerToPartyMap.put("C", "Green Party");
        answerToPartyMap.put("D", "Libertarian Party");

    }

    private static List<String> conductSurvey() {
        List<String> responses = new ArrayList<>();
        for (String question : questions) {
            System.out.println(question);
            System.out.print("Your answer: ");
            String answer = scanner.nextLine().trim().toUpperCase();
            responses.add(answer);
        }
        return responses;
    }

    private static String predictPoliticalParty(List<String> responses) {
        Map<String, Integer> partyCount = new HashMap<>();
        for (String response : responses) {
            String party = answerToPartyMap.getOrDefault(response, "Unknown");
            partyCount.put(party, partyCount.getOrDefault(party, 0) + 1);
        }
        return Collections.max(partyCount.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    private static void saveResponsesToFile(List<String> responses, String party) throws IOException {
        String filename;
        switch (party) {
            case "Democratic Party":
                filename = DEMOCRATIC_FILE;
                break;
            case "Republican Party":
                filename = REPUBLICAN_FILE;
                break;
            case "Green Party":
                filename = GREEN_FILE;
                break;
            case "Libertarian Party":
                filename = LIBERTARIAN_FILE;
                break;
            default:
                return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            for (String response : responses) {
                writer.write(response + "\n");
            }
            writer.write("\n");
        }
    }
}
