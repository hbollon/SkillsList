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
  int validation;
  String name;
  String desc;

  String block;

  Skill(String name, String desc, int validation) {
    this.name = name;
    this.desc = desc;
    this.validation = validation;
  }

  Skill.bis(String name, String desc, String block) {
    this.name = name;
    this.desc = desc;
    this.block = block;
  }
}
