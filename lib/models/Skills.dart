class SkillBlock {
  double value;
  String name;
  String desc;
  int sbId;

  SkillBlock(String name, String desc, double value, int sbId) {
    this.name = name;
    this.desc = desc;
    this.value = value;
    this.sbId = sbId;
  }
}

class Skill {
  bool obtained;
  String name;
  String desc;

  Skill(String name, String desc, bool obtained) {
    this.name = name;
    this.desc = desc;
    this.obtained = obtained;
  }
}
