import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Mental Health Companion Application
 * Demonstrates Object-Oriented Programming (OOP) principles:
 * - Encapsulation (private fields, getters/setters, data validation)
 * - Inheritance & Polymorphism (abstract WellnessResource with specialized subclasses)
 * - Abstraction (Trackable interface and abstract classes)
 */

// ========================== ABSTRACTION INTERFACE ==========================
interface Trackable {
    void displaySummary();
}

// ========================== ENCAPSULATED MODEL CLASSES ==========================

/**
 * MoodEntry represents an individual mood tracking record with timestamp.
 */
class MoodEntry implements Trackable {
    private final LocalDateTime timestamp;
    private final int moodScore; // 1 (Very Low) to 5 (Great)
    private final String feelingCategory;
    private final String note;

    public MoodEntry(int moodScore, String feelingCategory, String note) {
        if (moodScore < 1 || moodScore > 5) {
            throw new IllegalArgumentException("Mood score must be between 1 and 5.");
        }
        this.timestamp = LocalDateTime.now();
        this.moodScore = moodScore;
        this.feelingCategory = (feelingCategory == null || feelingCategory.trim().isEmpty()) ? "General" : feelingCategory.trim();
        this.note = (note == null || note.trim().isEmpty()) ? "No additional notes provided." : note.trim();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getMoodScore() {
        return moodScore;
    }

    public String getFeelingCategory() {
        return feelingCategory;
    }

    public String getNote() {
        return note;
    }

    @Override
    public void displaySummary() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.printf("[%s] Mood Score: %d/5 | Category: %-12s | Note: %s%n",
                timestamp.format(formatter), moodScore, feelingCategory, note);
    }
}

/**
 * WellnessGoal encapsulates a specific self-care or wellness objective.
 */
class WellnessGoal implements Trackable {
    private final String description;
    private boolean completed;

    public WellnessGoal(String description) {
        this.description = (description == null || description.trim().isEmpty()) ? "Unnamed Goal" : description.trim();
        this.completed = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public void displaySummary() {
        String statusSymbol = completed ? "[X] Completed" : "[ ] Pending";
        System.out.printf("%-15s - %s%n", statusSymbol, description);
    }
}

// ========================== INHERITANCE & POLYMORPHISM ==========================

/**
 * Abstract base class for wellness resources and exercises.
 */
abstract class WellnessResource {
    private final String title;
    private final String category;

    public WellnessResource(String title, String category) {
        this.title = title;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    // Abstract polymorphic method
    public abstract void practice();
}

/**
 * Concrete breathing exercise resource.
 */
class BreathingExercise extends WellnessResource {
    private final int durationMinutes;
    private final String breathingTechnique;

    public BreathingExercise(String title, int durationMinutes, String breathingTechnique) {
        super(title, "Breathing & Relaxation");
        this.durationMinutes = durationMinutes;
        this.breathingTechnique = breathingTechnique;
    }

    @Override
    public void practice() {
        System.out.println("=== " + getTitle() + " ===");
        System.out.println("Category: " + getCategory());
        System.out.println("Technique: " + breathingTechnique);
        System.out.println("Duration: " + durationMinutes + " minute(s)");
        System.out.println("Guidance: Inhale gently for 4s, hold for 4s, exhale slowly for 4s. Repeat calmly.");
    }
}

/**
 * Concrete mindfulness quote / affirmation resource.
 */
class AffirmationResource extends WellnessResource {
    private final String quote;
    private final String author;

    public AffirmationResource(String title, String quote, String author) {
        super(title, "Mindfulness & Affirmation");
        this.quote = quote;
        this.author = author;
    }

    @Override
    public void practice() {
        System.out.println("=== " + getTitle() + " ===");
        System.out.println("Category: " + getCategory());
        System.out.println("\"" + quote + "\"");
        System.out.println("- " + author);
    }
}

// ========================== SERVICE / MANAGER CLASS ==========================

/**
 * UserProfile manages collections of MoodEntries, Goals, and Resources.
 */
class UserProfile {
    private final String userName;
    private final List<MoodEntry> moodHistory;
    private final List<WellnessGoal> goals;
    private final List<WellnessResource> resources;

    public UserProfile(String userName) {
        this.userName = (userName == null || userName.trim().isEmpty()) ? "Friend" : userName.trim();
        this.moodHistory = new ArrayList<>();
        this.goals = new ArrayList<>();
        this.resources = new ArrayList<>();
        initializeDefaultResources();
    }

    public String getUserName() {
        return userName;
    }

    public List<MoodEntry> getMoodHistory() {
        return moodHistory;
    }

    public List<WellnessGoal> getGoals() {
        return goals;
    }

    public List<WellnessResource> getResources() {
        return resources;
    }

    public void addMoodEntry(MoodEntry entry) {
        moodHistory.add(entry);
    }

    public void addGoal(WellnessGoal goal) {
        goals.add(goal);
    }

    private void initializeDefaultResources() {
        resources.add(new BreathingExercise("Box Breathing Calm Down", 3, "4-4-4 Box Breathing"));
        resources.add(new BreathingExercise("4-7-8 Deep Sleep Relaxation", 5, "4-7-8 Respiratory Routine"));
        resources.add(new AffirmationResource("Daily Self-Compassion", "You do not have to be perfect to be worthy of peace and joy.", "Self-Care Reflection"));
        resources.add(new AffirmationResource("Grounding In The Present", "Breathe in peace, breathe out tension. This moment is enough.", "Mindful Practice"));
    }

    public double calculateAverageMood() {
        if (moodHistory.isEmpty()) {
            return 0.0;
        }
        int total = 0;
        for (MoodEntry entry : moodHistory) {
            total += entry.getMoodScore();
        }
        return (double) total / moodHistory.size();
    }
}

// ========================== MAIN APPLICATION CLASS ==========================

public class MentalHealthCompanion {
    private static final Scanner scanner = new Scanner(System.in);
    private static UserProfile profile;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("       WELCOME TO MENTAL HEALTH COMPANION         ");
        System.out.println("==================================================");
        System.out.print("Please enter your name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = "User";
        }
        profile = new UserProfile(name);

        System.out.println("\nHello, " + profile.getUserName() + "! We are glad to accompany you on your wellness journey.");

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Please select an option (1-6): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    logMood();
                    break;
                case "2":
                    viewMoodHistory();
                    break;
                case "3":
                    manageGoals();
                    break;
                case "4":
                    exploreWellnessResources();
                    break;
                case "5":
                    displayCompanionInsights();
                    break;
                case "6":
                    System.out.println("\nThank you for spending time on your well-being today, " + profile.getUserName() + ".");
                    System.out.println("Take gentle care of yourself. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid selection. Please choose an option between 1 and 6.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--------------------------------------------------");
        System.out.println("MAIN MENU - Mental Health Companion");
        System.out.println("--------------------------------------------------");
        System.out.println("1. Log Daily Mood & Reflection");
        System.out.println("2. View Mood History & Average");
        System.out.println("3. Manage Daily Wellness Goals");
        System.out.println("4. Guided Breathing & Mindfulness Resources");
        System.out.println("5. View Comprehensive Wellness Insights");
        System.out.println("6. Exit Application");
        System.out.println("--------------------------------------------------");
    }

    private static void logMood() {
        System.out.println("\n--- Log Your Mood ---");
        System.out.println("Rate your current mood (1 = Very Low, 2 = Low, 3 = Neutral, 4 = Good, 5 = Great):");
        
        int score = 0;
        while (score < 1 || score > 5) {
            System.out.print("Enter score (1-5): ");
            if (scanner.hasNextInt()) {
                score = scanner.nextInt();
                scanner.nextLine(); // clear buffer
                if (score < 1 || score > 5) {
                    System.out.println("Invalid score. Please choose a number from 1 to 5.");
                }
            } else {
                System.out.println("Please enter a valid numeric integer.");
                scanner.nextLine();
            }
        }

        System.out.print("Primary emotion/category (e.g., Calm, Stressed, Grateful, Tired): ");
        String category = scanner.nextLine().trim();

        System.out.print("Write a brief journal thought or note: ");
        String note = scanner.nextLine().trim();

        profile.addMoodEntry(new MoodEntry(score, category, note));
        System.out.println("Your mood has been logged successfully.");
    }

    private static void viewMoodHistory() {
        System.out.println("\n--- Mood History ---");
        List<MoodEntry> history = profile.getMoodHistory();
        if (history.isEmpty()) {
            System.out.println("No mood records logged yet. Try logging your mood today!");
            return;
        }

        for (MoodEntry entry : history) {
            entry.displaySummary();
        }

        System.out.printf("%nAverage Mood Score: %.2f / 5.00%n", profile.calculateAverageMood());
    }

    private static void manageGoals() {
        System.out.println("\n--- Wellness Goals Management ---");
        System.out.println("1. Add a New Goal");
        System.out.println("2. View & Toggle Goal Completion");
        System.out.print("Choose an option (1-2): ");
        String goalOption = scanner.nextLine().trim();

        if ("1".equals(goalOption)) {
            System.out.print("Enter goal description (e.g., 'Drink 2L water', 'Take a 20 min walk'): ");
            String desc = scanner.nextLine().trim();
            if (!desc.isEmpty()) {
                profile.addGoal(new WellnessGoal(desc));
                System.out.println("Goal added successfully!");
            } else {
                System.out.println("Goal description cannot be empty.");
            }
        } else if ("2".equals(goalOption)) {
            List<WellnessGoal> goals = profile.getGoals();
            if (goals.isEmpty()) {
                System.out.println("No goals tracked yet.");
                return;
            }

            System.out.println("\nCurrent Goals:");
            for (int i = 0; i < goals.size(); i++) {
                System.out.print((i + 1) + ". ");
                goals.get(i).displaySummary();
            }

            System.out.print("\nEnter goal number to toggle status (or press Enter to return): ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                try {
                    int index = Integer.parseInt(input) - 1;
                    if (index >= 0 && index < goals.size()) {
                        WellnessGoal selectedGoal = goals.get(index);
                        selectedGoal.setCompleted(!selectedGoal.isCompleted());
                        System.out.println("Updated status for: " + selectedGoal.getDescription());
                    } else {
                        System.out.println("Invalid goal index.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please provide a valid number.");
                }
            }
        } else {
            System.out.println("Returning to main menu.");
        }
    }

    private static void exploreWellnessResources() {
        System.out.println("\n--- Guided Resources & Exercises ---");
        List<WellnessResource> resources = profile.getResources();
        for (int i = 0; i < resources.size(); i++) {
            System.out.printf("%d. %s [%s]%n", (i + 1), resources.get(i).getTitle(), resources.get(i).getCategory());
        }

        System.out.print("\nSelect a resource to practice (1-" + resources.size() + "): ");
        String input = scanner.nextLine().trim();
        try {
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < resources.size()) {
                System.out.println();
                // Polymorphic execution
                resources.get(index).practice();
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid numeric choice.");
        }
    }

    private static void displayCompanionInsights() {
        System.out.println("\n================ COMPANION SUMMARY ================");
        System.out.println("User: " + profile.getUserName());
        System.out.println("Total Mood Logs: " + profile.getMoodHistory().size());
        if (!profile.getMoodHistory().isEmpty()) {
            System.out.printf("Overall Mood Average: %.2f / 5.0%n", profile.calculateAverageMood());
        }

        long completedGoals = profile.getGoals().stream().filter(WellnessGoal::isCompleted).count();
        System.out.println("Wellness Goals: " + completedGoals + " / " + profile.getGoals().size() + " completed");
        System.out.println("===================================================");
    }
}
