import highlighting.core.HighlightRegion;
import highlighting.regex.RegexHighlighter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RegexTest {

    RegexHighlighter h = new RegexHighlighter();

    @Test
    void einfache_faelle() {
        List<HighlightRegion> regions = h.computeRegions("public X @Override");

        assertFalse(regions.isEmpty());
        assertTrue(regions.stream().anyMatch(r -> r.start() == 0 && r.end() == 6));   // public
        assertTrue(regions.stream().anyMatch(r -> r.start() == 9 && r.end() == 18));  // @Override
    }

    @Test
    void ueberlappung_keyword() {
        String text = "// public class";
        List<HighlightRegion> regions = h.computeRegions(text);

        // Es soll nur die Kommentar-Region bleiben
        assertEquals(1, regions.size());
        assertEquals(0, regions.get(0).start());
        assertEquals(text.length(), regions.get(0).end());
    }


    @Test
    void angrenzende_regionen() {
        List<HighlightRegion> normalized = List.of(
            new HighlightRegion(0, 5, java.awt.Color.BLUE),
            new HighlightRegion(5, 10, java.awt.Color.RED)
        );

        List<HighlightRegion> resolved = h.resolveConflicts(normalized);

        assertEquals(2, resolved.size()); // [0,5) und [5,10) überlappen nicht
    }

    @Test
    void leer_und_kein_match() {
        assertTrue(h.computeRegions("").isEmpty());
        assertTrue(h.computeRegions("xyz ???").isEmpty());
    }
}
