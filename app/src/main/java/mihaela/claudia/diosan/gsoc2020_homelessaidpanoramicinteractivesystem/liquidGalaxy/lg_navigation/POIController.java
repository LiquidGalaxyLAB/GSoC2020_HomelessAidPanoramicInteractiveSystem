package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_navigation;


import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGCommand;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.lg_connection.LGConnectionManager;

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

    private POIController() {
        currentPOI = EARTH_POI;
        moveToPOI(EARTH_POI, null);
    }

    public LGCommand moveToPOI(POI poi, LGCommand.Listener listener) {
        //previousPOI = new POI(currentPOI);
        currentPOI = new POI(poi);
        return sendPoiToLG(listener);
    }

    public LGCommand showPlacemark(POI poi, LGCommand.Listener listener){
        currentPOI = new POI(poi);
        return  sendPlacemarkToLG(listener);
    }

    public LGCommand sendKML(POI poi, LGCommand.Listener listener){
        currentPOI = new POI(poi);
        return  setKML(listener);
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

    private LGCommand sendPlacemarkToLG(LGCommand.Listener listener){
        LGCommand lgCommand = new LGCommand(buildPlacemark(), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            //currentPOI = new POI(previousPOI);
            if(listener != null)
                listener.onResponse(result);
        });
        LGConnectionManager.getInstance().addCommandToLG(lgCommand);
        return lgCommand;
    }

    private LGCommand setKML(LGCommand.Listener listener){
        LGCommand lgCommand = new LGCommand(setKmlRoute(), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            //currentPOI = new POI(previousPOI);
            if(listener != null)
                listener.onResponse(result);
        });
        LGConnectionManager.getInstance().addCommandToLG(lgCommand);
        return lgCommand;
    }



    private static String buildPlacemark(){
        return  "echo '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\"> <Placemark>\n" +
                " <name>Marca de posición simple</name>\n" +
                " <description>Pegada al suelo. Se coloca de forma inteligente a la altura del relieve subyacente.</description>\n" +
                " <Point>\n" +
                " <coordinates>-122.0822035425683,37.42228990140251,0</coordinates>\n" +
                " </Point>\n" +
                " </Placemark> </kml>' > /var/www/html/placemarks.kml";
    }

    private static String setKmlRoute(){
        return "echo '/var/www/html/placemarks.kml' > /var/www/html/kmls.txt";
    }

}
