class User {
  int dbId;
  String username;
  String firstName;
  String lastName;

  static User loggedInUser;

  User(int dbId, String username, String firstName, String lastName) {
    this.dbId = dbId;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
