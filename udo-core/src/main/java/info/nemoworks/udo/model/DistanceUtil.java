package info.nemoworks.udo.model;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;

public class DistanceUtil {

    // 计算经纬度间实际距离，误差与高德地图实际距离相差1m内
    public static float getDistance(float longitudeFrom, float latitudeFrom, float longitudeTo,
        float latitudeTo) {
        GlobalCoordinates source = new GlobalCoordinates(latitudeFrom, longitudeFrom);
        GlobalCoordinates target = new GlobalCoordinates(latitudeTo, longitudeTo);
        return (float) new GeodeticCalculator()
            .calculateGeodeticCurve(Ellipsoid.Sphere, source, target).getEllipsoidalDistance();
    }
}
