public class CustomTable {

    private String month, xlabel, ylabel;
    private int x, y;

    public CustomTable() {
        this.month = null;
        this.x = 0;
        this.y = 0;
    }

    public CustomTable(String month, int x, int y) {
        this.month = month;
        this.x = x;
        this.y = y;
    }

    public void distribute(CustomTable[] table, String line, String prop) {
        if (prop.equalsIgnoreCase("x")) {
            switch (line.split("-")[1]) {
                case "06" -> table[0].add_x();
                case "07" -> table[1].add_x();
                case "08" -> table[2].add_x();
                case "09" -> table[3].add_x();
                case "10" -> table[4].add_x();
                case "11" -> table[5].add_x();
                case "12" -> table[6].add_x();
                default -> {}
            }
        }
        else if (prop.equalsIgnoreCase("y")) {
            switch (line.split("-")[1]) {
                case "06" -> table[0].add_y();
                case "07" -> table[1].add_y();
                case "08" -> table[2].add_y();
                case "09" -> table[3].add_y();
                case "10" -> table[4].add_y();
                case "11" -> table[5].add_y();
                case "12" -> table[6].add_y();
                default -> {}
            }
        }
    }

    private void add_x() { this.x++; }
    private void add_y() { this.y++; }
    public void set_xlabel(String xlabel) { this.xlabel = xlabel; }
    public void set_ylabel(String ylabel) { this.ylabel = ylabel; }

    public String toString() { return String.format("%s: %d %s, %d %s", this.month, this.x, this.xlabel, this.y, this.ylabel); }
}