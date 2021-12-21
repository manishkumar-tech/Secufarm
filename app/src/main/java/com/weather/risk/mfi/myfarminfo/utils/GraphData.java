package com.weather.risk.mfi.myfarminfo.utils;

public class GraphData {

    public static String setGraph(String Heading, String[] Labels, String[] Value, String MaxValue, String[] ColorCode) {
        String XaxissubHeading = "";
        String YaxisHeading = "Values";
        String dataValue = "[{ \"value\": \"" + Value[0] + "\",\"color\":\"" + ColorCode[0] + "\",\"label\":\"" + Labels[0] + "\"},{ \"value\": \"" + Value[1] + "\",\"color\":\"" + ColorCode[1] + "\",\"label\":\"" + Labels[1] + "\"}]";

        String data = "<html>\n" +
                "<head>\n" +
                "\t<title>My first chart using FusionCharts Suite XT</title>\n" +
                "\t<script type=\"text/javascript\" src=\"file:///android_asset/js/fusioncharts.js\"></script>\n" +
                "\t<script type=\"text/javascript\" src=\"file:///android_asset/js/fusioncharts.charts.js\"></script>\n" +
                "\t<script type=\"text/javascript\" src=\"file:///android_asset/js/themes/fusioncharts.theme.fint.js\"></script>\n" +
                "\t<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/js/main.css\">\n" +
                "\t<script type=\"text/javascript\">\n" +
                "\tFusionCharts.ready(function(){\n" +
                "    var revenueChart = new FusionCharts({\n" +
                "        \"type\": \"column2d\",\n" +
                "        \"renderAt\": \"chartContainer\",\n" +
                "        \"width\": \"100%\",\n" +
                "        \"height\": \"200\",\n" +
                "        \"dataFormat\": \"json\",\n" +
                "        \"dataSource\": {\n" +
                "            \"chart\": {\n" +
                "                \"caption\": \"" + Heading + "\",\n" +
                "                \"subCaption\": \"" + XaxissubHeading + "\",\n" +
                "                \"palettecolors\": \"#4DFF9800\",\n" +
                "                \"useplotgradientcolor\": \"0\",\n" +
                "                \"yAxisName\": \"" + YaxisHeading + "\",\n" +
                "                \"theme\": \"fint\",\n" +
                "                \"bgColor\": \"#ffffff\",\n" +
                "                \"showvalues\": \"0\",\n" +
                "                \"yaxismaxvalue\": \"" + MaxValue + "\",\n" +
                "                \"yaxisminvalue\": \"0\"\n" +
                "            },\n" +
                "            \"data\":" + dataValue + " \n" +
                "        }\n" +
                "    });\n" +
                "    revenueChart.render();\n" +
                "\t})\n" +
                "\t</script>\n" +
                "</head>\n" +
                "<style> body{ margin:0px" +
                "}</style>\n" +
                "<body>\n" +
                "<div id=\"chartContainer\">FusionCharts XT will load here!</div>\n" +
                "</body>\n" +
                "</html>";


        return data;
    }
}
