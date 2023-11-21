# Define the User class to represent each user of the app
class User:
    # Constructor to initialize a new user object with relevant attributes
    def __init__(
        self,
        name,
        weight,
        activity_level,
        is_athlete,
        training_intensity=None,
        training_duration=0,
        environment=None,
    ):
        self.name = name
        self.weight = weight
        self.activity_level = activity_level
        self.is_athlete = is_athlete
        self.training_intensity = training_intensity
        self.training_duration = training_duration
        self.environment = environment
        self.water_intake_goal = 0
        self.current_intake = 0
        self.calculate_hydration_goal()

    # Method to calculate the user's daily hydration goal
    def calculate_hydration_goal(self):
        # Calculate differently if the user is an athlete
        if self.is_athlete:
            # Base calculation on weight and adjust for training intensity, duration, and environment
            self.water_intake_goal = self.weight * 35
            if self.training_intensity == "high":
                self.water_intake_goal *= 1.2
            if self.training_duration >= 2:
                self.water_intake_goal *= 1.15
            if self.environment == "hot":
                self.water_intake_goal *= 1.2

        else:
            # For non-athletes, adjust based on weight and activity level
            self.water_intake_goal = self.weight * (
                35 if self.activity_level == "moderate" else 30
            )

    # Method for users to log their water intake
    def log_intake(self, amount):
        self.current_intake += amount

    # Method to check and return the user's current progress
    def check_progress(self):
        return self.current_intake, self.water_intake_goal


# Define the HydrationApp class to manage multiple users
class HydrationApp:
    def __init__(self):
        self.user_list = {}  # Dictionary to store users

    # Method to add a new user to the app
    def add_user(self, user):
        self.user_list[user.name] = user

    # Method to retrieve a user object by name
    def get_user(self, name):
        return self.user_list.get(name)

    # Method to display a user's progress
    def display_progress(self, user):
        current_intake, goal = user.check_progress()
        print(
            f"{user.name}, you have consumed {current_intake} ml of your {goal} ml daily goal."
        )


# Main function to run the hydration tracking program
def main():
    app = HydrationApp()  # Create an instance of the HydrationApp
    print("Welcome to Hydration Tracker")

    # Main loop to interact with the user
    while True:
        # Prompt user for action
        action = input(
            "Choose action: add user, log intake, check progress, or exit: "
        ).lower()

        # Handle the 'add user' action
        if action == "add user":
            # Collect necessary information for creating a new user
            name = input("Enter name: ")
            weight = float(input("Enter weight (kg): "))
            activity_level = input("Enter activity level (low/moderate/high): ").lower()
            is_athlete = input("Are you an athlete? (yes/no): ").lower() == "yes"
            # Additional information required if the user is an athlete
            if is_athlete:
                training_intensity = input(
                    "Enter training intensity (low/moderate/high): "
                ).lower()
                training_duration = int(input("Enter training duration (hours): "))
                environment = input("Enter environment (normal/hot/cold): ").lower()
                user = User(
                    name,
                    weight,
                    activity_level,
                    is_athlete,
                    training_intensity,
                    training_duration,
                    environment,
                )
            else:
                user = User(name, weight, activity_level, is_athlete)

            app.add_user(user)

        # Handle the 'log intake' action
        elif action == "log intake":
            # Prompt for user's name and the amount of water intake
            name = input("Enter your name: ")
            amount = float(input("Enter the amount of water you drank (ml): "))
            user = app.get_user(name)
            if user:
                user.log_intake(amount)
            else:
                print("User not found.")
        # Handle the 'check progress' action
        elif action == "check progress":
            # Prompt for the user's name and display their progress
            name = input("Enter your name: ")
            user = app.get_user(name)
            if user:
                app.display_progress(user)
            else:
                print("User not found.")

        # Exit the program if the 'exit' action is chosen
        elif action == "exit":
            break


if __name__ == "__main__":
    main()
