package sh.now.afk.processing;

import processing.core.PApplet;

public class ColorfulEmoji extends PApplet {
    @Override
    public void settings() {
        size(700, 600);
        noLoop();
    }

    @Override
    public void setup() {
        background(244);
        textFont(createFont("Segoe UI Emoji", 50));
        String s = """
                🈷🈶🈯🉐🈹🈚🈲🉑🈸
                🈴🈳㊗㊙🈺🈵
                🔴🟠🟡🟢🔵🟣🟤⚫⚪
                🟥🟧🟨🟩🟦🟪🟫⬛⬜
                ◼◻◾◽▪▫🔶🔷🔸🔹🔺
                🔻💠🔘🔳🔲""";
        text(s, 60, 60); // rows and columns
    }

    public static void main(String[] args) {
        PApplet.runSketch(new String[]{"", ""}, new ColorfulEmoji());
    }
}
