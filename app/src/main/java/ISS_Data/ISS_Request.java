package ISS_Data;

/**
 * Created by tanma on 3/27/2018.
 */

public class ISS_Request {
    private double longitude;
    private double lattitude;
    private double altitude;
    private int n;

    public  ISS_Request(boolean currentLocation)
    {


    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }


    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }


    public void setN(int n) {
        this.n = n;
    }


    public String constructQuerry()
    {
        String querry;

        if(altitude<=0)
        {
            querry = new String("http://api.open-notify.org/iss-pass.json?"+"lat="+lattitude+"&lon="+longitude+"&alt="+"&n="+n);

        }
        else {
            querry = new String("http://api.open-notify.org/iss-pass.json?" + "lat=" + lattitude + "&lon=" + longitude + "&alt=" + altitude + "&n=" + n);
        }
        return querry;
    }



}



