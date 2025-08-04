public class RouteInfo {
    private final String origin;
    private final String destination;
    private final String fullNumber;
    private final Integer primaryNumber;

    public RouteInfo(String origin, String destination, String number) {
        this.origin = origin;
        this.destination = destination;
        this.fullNumber = number;
        this.primaryNumber = (int) number.charAt(0);
    }

    public String getOrigin() {
        return origin;
    }

    public Boolean isOrigin(String test) {
        return origin.equals(test);
    }

    public String getDestination() {
        return destination;
    }

    public Boolean isDestination(String test) {
        return destination.equals(test);
    }

    public String getFullNumber() {
        return fullNumber;
    }

    public Boolean isFullNumber(String test) {
        return fullNumber.equals(test);
    }

    public Integer getPrimaryNumber() {
        return primaryNumber;
    }

    public Boolean isPrimaryNumber(Integer test) {
        return primaryNumber.equals(test);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == RouteInfo.class) {
            RouteInfo route = ((RouteInfo) obj);
            return route.isOrigin(origin) && route.isDestination(destination);
        }
        return false;
    }
}
