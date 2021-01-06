package Day19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// Class to hold rule, can be one of two types: SUB_RULE or CHAR_MATCH. Either holds a character
// to match, or a list of sub-rules to match
class Rule {

  RuleType type;
  List<List<Integer>> subRules;
  char matchChar;

  public Rule(char matchChar) {
    type = RuleType.CHAR_MATCH;
    this.matchChar = matchChar;
    subRules = null;
  }

  public Rule(List<List<Integer>> subRules) {
    type = RuleType.SUB_RULE;
    this.subRules = subRules;
  }

  // Boring parsing code which returns a RuleInfo class - which contains the parsed rule, and its
  // rule number (which can then be used as an index into a RuleSet)
  public static RuleInfo parseRule(String rawRule) {
    Pattern p = Pattern.compile("(\\d+): (\"(.)\"|(.*))");
    Matcher m = p.matcher(rawRule);
    m.find();
    int ruleNo = Integer.parseInt(m.group(1));
    if (m.group(3) != null) {
      return new RuleInfo(ruleNo, new Rule(m.group(3).charAt(0)));
    } else {
      String[] subRuleGroups = m.group(2).split("\\|");
      List<List<Integer>> subRules = new ArrayList<>();
      for (String subRuleGroup : subRuleGroups) {
        subRules.add(
            Arrays.stream(subRuleGroup.trim().split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toList()));
      }
      return new RuleInfo(ruleNo, new Rule(subRules));
    }
  }

  enum RuleType {
    SUB_RULE,
    CHAR_MATCH
  }

  public static class RuleInfo {

    private final int ruleNumber;
    private final Rule rule;

    public RuleInfo(int ruleNumber, Rule rule) {
      this.ruleNumber = ruleNumber;
      this.rule = rule;
    }

    public int getRuleNumber() {
      return ruleNumber;
    }

    public Rule getRule() {
      return rule;
    }
  }
}
