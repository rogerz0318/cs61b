public class Planet {
    public double xxPos, yyPos, xxVel, yyVel, mass;
    public String imgFileName;
    public static final double G = 6.67e-11;

    public Planet(double xxPos, double yyPos, double xxVel, double yyVel, double mass, String imgFileName) {
        this.xxPos = xxPos;
        this.yyPos = yyPos;
        this.xxVel = xxVel;
        this.yyVel = yyVel;
        this.mass = mass;
        this.imgFileName = imgFileName;
    }

    public Planet(Planet p) {
        this.xxPos = p.xxPos;
        this.yyPos = p.yyPos;
        this.xxVel = p.xxVel;
        this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }

    /**
     * Calculates the distance between this planet and the planet p.
     * @param p the other planet instance
     * @return the distance between the two planets
     */
    public double calcDistance(Planet p) {
        double dx = (xxPos - p.xxPos);
        double dy = (yyPos - p.yyPos);
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calculates the total force exerted by another planet p.
     * @param p the other planet instance
     * @return a positive total exerted force
     */
    public double calcForceExertedBy(Planet p) {
        double d = calcDistance(p);
        if (d == 0) return 0;
        return G * mass * p.mass / (d * d);
    }

    /**
     * Calculates the force in x-direction exerted by another planet p.
     * @param p the other planet instance
     * @return exerted force in x-direction, positive if pointing to +x and vice versa
     */
    public double calcForceExertedByX(Planet p) {
        double d = calcDistance(p);
        if (d == 0) return 0;
        return calcForceExertedBy(p) * (p.xxPos - xxPos) / d;
    }

    /**
     * Calculates the force in y-direction exerted by another planet p.
     * @param p the other planet instance
     * @return exerted force in y-direction, positive if pointing to +y and vice versa
     */
    public double calcForceExertedByY(Planet p) {
        double d = calcDistance(p);
        if (d == 0) return 0;
        return calcForceExertedBy(p) * (p.yyPos - yyPos) / d;
    }

    /**
     * Calculates the net force in x-direction exerted by an array of planets.
     * @param planets an array of planets
     * @return the net force exerted on this planet by the array of planets
     */
    public double calcNetForceExertedByX(Planet[] planets) {
        double f = 0;
        for (Planet p: planets) {
            f += calcForceExertedByX(p);
        }
        return f;
    }

    /**
     * Calculates the net force in y-direction exerted by an array of planets.
     * @param planets an array of planets
     * @return the net force exerted on this planet by the array of planets
     */
    public double calcNetForceExertedByY(Planet[] planets) {
        double f = 0;
        for (Planet p: planets) {
            f += calcForceExertedByY(p);
        }
        return f;
    }

    /**
     * Updates the planet's location after a short period of time.
     * @param dt a short time period
     * @param fx x-directional force exerted on this planet
     * @param fy y-directional force exerted on this planet
     */
    public void update(double dt, double fx, double fy) {
        xxVel += (fx / mass) * dt;
        xxPos += xxVel * dt;
        yyVel += (fy / mass) * dt;
        yyPos += yyVel * dt;
    }
}
