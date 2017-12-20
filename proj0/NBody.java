public class NBody {

    public static double readRadius(String filePath) {
        In in = new In(filePath);
        in.readInt(); // number of planets
        return in.readDouble();
    }

    public static Planet[] readPlanets(String filePath) {
        In in = new In(filePath);
        int n = in.readInt(); // number of planets
        in.readDouble(); // radius of universe
        Planet[] planets = new Planet[n];
        for (int i = 0; i < n; i++) {
            double xxPos = in.readDouble();
            double yyPos = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String imgFileName = in.readString();
            planets[i] = new Planet(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
        }
        return planets;
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("ERROR: Must have three arguments: tEnd, dt, and filename.");
            System.exit(1);
        }

        // Read in arguments
        double tEnd = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String planetsFileName = args[2];

        // Get radius and planets from the file
        double radius = readRadius(planetsFileName);
        // System.out.println("Universe radius = " + radius);
        Planet[] planets = readPlanets(planetsFileName);

        // Draw the background picture
        String backgroundPath = "./images/starfield.jpg";
        StdDraw.setScale(-radius, radius);
        StdDraw.clear();
        StdDraw.picture(0, 0, backgroundPath);

        // Draw planets
        for (Planet p: planets) {
            // System.out.println(p.imgFileName + ", x = " + p.xxPos + ", y = " + p.yyPos);
            p.draw();
        }
        StdDraw.show();

        // animation
        double t = 0;
        while (t < tEnd) {
            // Calculates forces for each planet
            double[] xForces = new double[planets.length];
            double[] yForces = new double[planets.length];
            for (int i = 0; i < planets.length; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }

            // Update planets
            for (int i = 0; i < planets.length; i++) {
                planets[i].update(dt, xForces[i], yForces[i]);
            }

            // Redraw
            StdDraw.clear();
            StdDraw.picture(0, 0, backgroundPath);
            for (Planet p: planets) {
                p.draw();
            }
            StdDraw.show(10);
            t += dt;
        }
    }
}
