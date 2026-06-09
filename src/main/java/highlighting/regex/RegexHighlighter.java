package highlighting.regex;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaTokens;
import java.util.ArrayList;
import java.util.List;

public class RegexHighlighter extends SyntaxHighlighter {

  @Override
  public List<HighlightRegion> collectMatches(String text) {
    var candidates = new ArrayList<HighlightRegion>();
    for (var token : MiniJavaTokens.defaultTokens()) {
      candidates.addAll(token.test(text));
    }
    return candidates;
  }

  @Override
  public List<HighlightRegion> resolveConflicts(List<HighlightRegion> normalized) {
    var resolved = new ArrayList<HighlightRegion>();
    HighlightRegion lastKept = null;

    for (HighlightRegion region : normalized) {
      if (lastKept == null || region.start() >= lastKept.end()) {
        resolved.add(region);
        lastKept = region;
      }
    }

    return resolved;
  }
}
