package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation;


import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGConnectionManager;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.logic.Homeless;

public class POIController {

    private static POIController INSTANCE = null;

    public static final POI EARTH_POI = new POI()
            .setLongitude(10.52668d)
            .setLatitude(40.085941d)
            .setAltitude(0.0d)
            .setRange(10000000.0d)
            .setAltitudeMode("relativeToSeaFloor");

    public synchronized static POIController getInstance() {
        if (INSTANCE == null)
            INSTANCE = new POIController();
        return INSTANCE;
    }

    private POI currentPOI;
    //private POI previousPOI;
    private Homeless homeless;

    private POIController() {
        currentPOI = EARTH_POI;
        moveToPOI(EARTH_POI, null);
    }

    public LGCommand moveToPOI(POI poi, LGCommand.Listener listener) {
        //previousPOI = new POI(currentPOI);
        currentPOI = new POI(poi);
        return sendPoiToLG(listener);
    }

    public LGCommand showPlacemark(POI poi, LGCommand.Listener listener, String placemarkIcon, String route){
        currentPOI = new POI(poi);
        return  sendPlacemarkToLG(listener, placemarkIcon, route);
    }

    public LGCommand showBalloon(POI poi, LGCommand.Listener listener,String description, String image, String route){
        currentPOI = new POI(poi);
        return  sendBalloonToLG(listener,description, image, route);
    }

    public LGCommand sendPlacemark(POI poi, LGCommand.Listener listener, String hostIp, String route){
        currentPOI = new POI(poi);
        return  setPlacemark(listener, hostIp, route);
    }

    public LGCommand sendBalloon(POI poi, LGCommand.Listener listener, String route){
        currentPOI = new POI(poi);
        return  setBalloon(listener, route);
    }


    public synchronized void moveXY(double angle, double percentDistance) {
        //.setLongitude() [-180 to +180]: X (cos)
        //.setLatitude() [-90 to +90]: Y (sin)

        /*POI newPoi = new POI(currentPOI);
        //0.0001% of RANGE
        double STEP_XY = 0.000001;
        newPoi.setLongitude(newPoi.getLongitude() + Math.cos(angle) * percentDistance * STEP_XY * newPoi.getRange());
        while (newPoi.getLongitude() > 180) {
            newPoi.setLongitude(newPoi.getLongitude() - 360);
        }
        while (newPoi.getLongitude() < -180) {
            newPoi.setLongitude(newPoi.getLongitude() + 360);
        }

        newPoi.setLatitude(newPoi.getLatitude() - Math.sin(angle) * percentDistance * STEP_XY * newPoi.getRange());
        while (newPoi.getLatitude() > 90) {
            newPoi.setLatitude(newPoi.getLatitude() - 180);
        }
        while (newPoi.getLatitude() < -90) {
            newPoi.setLatitude(newPoi.getLatitude() + 180);
        }

        moveToPOI(newPoi, null);*/
    }

    public synchronized void moveCameraAngle(double angle, double percentDistance) {
        //.setTilt() [0 to 90]: the angle between what you see and the earth (90 means you see horizon) (the sin of the angle)
        //.setHeading() [-180 to 180]: compass degrees (the cos of the angle)
    }

    public synchronized void zoomIn(double percent) {
        //.setRange() [0 to 999999]
    }

    public synchronized void zoomOut(double percent) {
        //.setRange() [0 to 999999]
    }

    private LGCommand sendPoiToLG(LGCommand.Listener listener) {
        LGCommand lgCommand = new LGCommand(buildCommand(currentPOI), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            //currentPOI = new POI(previousPOI);
            if(listener != null)
                listener.onResponse(result);
        });
        LGConnectionManager.getInstance().addCommandToLG(lgCommand);
        return lgCommand;
    }

    private static String buildCommand(POI poi) {
        //return "echo 'flytoview=<gx:duration>3</gx:duration><gx:flyToMode>smooth</gx:flyToMode><LookAt><longitude>" + poi.getLongitude() + "</longitude><latitude>" + poi.getLatitude() + "</latitude><altitude>" + poi.getAltitude() + "</altitude><heading>" + poi.getHeading() + "</heading><tilt>" + poi.getTilt() + "</tilt><range>" + poi.getRange() + "</range><gx:altitudeMode>" + poi.getAltitudeMode() + "</gx:altitudeMode></LookAt>' > /tmp/query.txt";

        return "echo 'flytoview=<gx:duration>3</gx:duration><gx:flyToMode>smooth</gx:flyToMode><LookAt>" +
                "<longitude>" + poi.getLongitude() + "</longitude>" +
                "<latitude>" + poi.getLatitude() + "</latitude>" +
                "<altitude>" + poi.getAltitude() + "</altitude>" +
                "<range>" + poi.getRange() + "</range>" +
                "<gx:altitudeMode>" + poi.getAltitudeMode() + "</gx:altitudeMode>" +
                "</LookAt>' > /tmp/query.txt";
    }

    private LGCommand sendPlacemarkToLG(LGCommand.Listener listener, String placemarkIcon, String route){
        LGCommand lgCommand = new LGCommand(buildPlacemark(currentPOI, placemarkIcon, route), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            //currentPOI = new POI(previousPOI);
            if(listener != null)
                listener.onResponse(result);
        });
        LGConnectionManager.getInstance().addCommandToLG(lgCommand);
        return lgCommand;
    }

    private LGCommand sendBalloonToLG(LGCommand.Listener listener,String description, String image, String route){
        LGCommand lgCommand = new LGCommand(buildDescriptionBallon(currentPOI,description, image, route), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            //currentPOI = new POI(previousPOI);
            if(listener != null)
                listener.onResponse(result);
        });
        LGConnectionManager.getInstance().addCommandToLG(lgCommand);
        return lgCommand;
    }

    private LGCommand setPlacemark(LGCommand.Listener listener, String hostIp, String route){
        LGCommand lgCommand = new LGCommand(setPlacemarkRoute(currentPOI, hostIp,route), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            //currentPOI = new POI(previousPOI);
            if(listener != null)
                listener.onResponse(result);
        });
        LGConnectionManager.getInstance().addCommandToLG(lgCommand);
        return lgCommand;
    }

    private LGCommand setBalloon(LGCommand.Listener listener, String route){
        LGCommand lgCommand = new LGCommand(setBalloonRoute(currentPOI,route), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            //currentPOI = new POI(previousPOI);
            if(listener != null)
                listener.onResponse(result);
        });
        LGConnectionManager.getInstance().addCommandToLG(lgCommand);
        return lgCommand;
    }



    private static String buildPlacemark(POI poi, String placemarkIcon, String route){
       return "echo '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<kml xmlns=\"http://www.opengis.net/kml/2.2\"> " +
               "<Placemark>\n" +
               "  <Style id=\"homelessIcon\">\n" +
               "      <IconStyle>\n" +
               "        <Icon>\n" +
               "          <href>" + placemarkIcon + "</href>\n" +
               "        </Icon>\n" +
               "      </IconStyle>\n" +
               "    </Style>\n" +
               "  <styleUrl>#homelessIcon</styleUrl>\n" +
               " <Point>\n" +
               " <coordinates>" + poi.getLongitude() + "," + poi.getLatitude() + "," + poi.getAltitude() + "</coordinates>\n" +
               " </Point>\n" +
               " </Placemark> </kml>' > /var/www/html/hapis/" + route + "/" + poi.getName() + ".kml";
    }


    private static String setPlacemarkRoute(POI poi, String hostIp, String route){
        return "echo 'http://" + hostIp + ":81/hapis/" + route + "/" + poi.getName() + ".kml' >> /var/www/html/kmls.txt";
    }

    private static String setBalloonRoute(POI poi,String route){
        return "echo 'http://localhost:81/hapis/" + route + "/" + poi.getName() + ".kml' >> /var/www/html/kmls.txt";
    }

    public static void cleanKm(){
        String sentence = "chmod 777 /var/www/html/kmls.txt; echo '' > /var/www/html/kmls.txt";
        LGConnectionManager.getInstance().addCommandToLG(new LGCommand(sentence, LGCommand.CRITICAL_MESSAGE, null));
    }

    private static String buildDescriptionBallon(POI poi, String description, String image, String route ){
       return  "echo '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<kml xmlns=\"http://www.opengis.net/kml/2.2\"\n" +
               "  xmlns:gx=\"http://www.google.com/kml/ext/2.2\">\n" +
               "  \n" +
               "    <Placemark>\n" +
               "      <name>" + poi.getName() + "</name>\n" +
               "\t<gx:Carousel>\n" +
               "\t\t\t<gx:Image kml:id=\"embedded_image_0EC545829414BC60CDE6\">\n" +
               "\t\t\t\t<gx:ImageUrl>" + image + "</gx:ImageUrl>\n"+
               "</gx:Image>\n" +
               "\t\t</gx:Carousel>\n" +
               "      <description>\n" +
               "        <![CDATA[\n" +
               "<body style=\"width:500px; height:550px\"> " + description +
               "</body>" +
               "        ]]> \n" +
               "      </description>\n" +
               " <gx:displayMode>panel</gx:displayMode>" +
               "      <gx:balloonVisibility>1</gx:balloonVisibility>\n" +
               "      <Point>\n" +
               "        <coordinates>" + poi.getLongitude() + "," + poi.getLatitude() + "," + poi.getAltitude() + "</coordinates>\n" +
               "      </Point>\n" +
               "    </Placemark>\n" +
               "    \n" +
               "</kml>' > /var/www/html/hapis/" + route + "/" + poi.getName() + ".kml";
    }


}
