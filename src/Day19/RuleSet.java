package Day19;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Internally, simply just a map from integer (rule number) to rule
class RuleSet {

  Map<Integer, Rule> ruleSet;

  private RuleSet(Map<Integer, Rule> ruleSet) {
    this.ruleSet = ruleSet;
  }

  public void setRule(String rawRule) {
    Rule.RuleInfo ruleInfo = Rule.parseRule(rawRule);
    ruleSet.put(ruleInfo.getRuleNumber(), ruleInfo.getRule());
  }

  // Simply calls parseRule on each raw rule, and inserts it into the ruleSet map according
  // to the received RuleInfo
  public static RuleSet parseRules(List<String> rawRules) {
    Map<Integer, Rule> ruleSet =
        rawRules.stream()
            .map(Rule::parseRule)
            .collect(Collectors.toMap(Rule.RuleInfo::getRuleNumber, Rule.RuleInfo::getRule));
    return new RuleSet(ruleSet);
  }

  public boolean matches(List<Integer> ruleList, String message) {
    if (ruleList.isEmpty()) {
      return message.isEmpty(); // no more rules to match - is message empty?
    }
    Rule curRule = ruleSet.get(ruleList.get(0));
    List<Integer> nextRules = ruleList.subList(1, ruleList.size());
    return switch (curRule.type) {
      // Matches if the first character matches, and the rest of the message matches the rest of the rules
      case CHAR_MATCH -> !message.isEmpty()
          && (message.charAt(0) == curRule.matchChar)
          && matches(nextRules, message.substring(1));
      // Matches if any of the subRule groups match
      // A subRule group matches if each of its subRules match, and all of the nextRules match
      case SUB_RULE -> curRule.subRules.stream()
          .map( // Concatenate this subRuleGroup's rules and nextRules
              l ->
                  Stream.of(l, nextRules)
                      .flatMap(Collection::stream)
                      .collect(Collectors.toList()))
          .map(l -> matches(l, message))
          .reduce(false, (a, b) -> a || b);
    };
  }
}
