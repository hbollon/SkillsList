class UserRole {
  String name;
  bool canValidate;
  bool canAddSkill;

  UserRole(String name, bool canValidate, bool canAddSkill) {
    this.canAddSkill = canAddSkill;
    this.name = name;
    this.canValidate = canValidate;
  }
}

class User {
  int dbId;
  String username;
  String firstName;
  String lastName;

  UserRole role;

  static User loggedInUser;

  User(int dbId, String username, String firstName, String lastName,
      UserRole role) {
    this.dbId = dbId;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role;
  }
}
