import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Main class to run the physics engine
public class PhysicsEnginePrototype extends JPanel {
    private final List<PhysicsObject> objects = new ArrayList<>();
    private final double GRAVITY = 9.8; // Gravity constant

    public PhysicsEnginePrototype() {
        // Initialize some objects
        objects.add(new PhysicsObject(50, 50, 20, 2, Color.RED));
        objects.add(new PhysicsObject(200, 100, 30, 3, Color.BLUE));
        objects.add(new PhysicsObject(300, 150, 25, 1, Color.GREEN));

        // Timer for the game loop
        Timer timer = new Timer(16, e -> {
            updatePhysics(0.016); // Update physics, assuming ~60 FPS
            repaint(); // Repaint the screen
        });
        timer.start();
    }

    // Update the physics
    private void updatePhysics(double deltaTime) {
        for (PhysicsObject obj : objects) {
            // Apply gravity
            obj.applyForce(0, GRAVITY * obj.getMass());

            // Update position and velocity
            obj.update(deltaTime);

            // Handle collision with window boundaries
            handleBoundaryCollision(obj);
        }
    }

    // Handle collisions with the window boundaries
    private void handleBoundaryCollision(PhysicsObject obj) {
        if (obj.getX() - obj.getRadius() < 0 || obj.getX() + obj.getRadius() > getWidth()) {
            obj.reverseVelocityX();
        }
        if (obj.getY() - obj.getRadius() < 0 || obj.getY() + obj.getRadius() > getHeight()) {
            obj.reverseVelocityY();
        }
    }

    // Paint the objects
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Clear the screen
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw each object
        for (PhysicsObject obj : objects) {
            obj.draw(g);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Physics Engine Prototype");
        PhysicsEnginePrototype engine = new PhysicsEnginePrototype();

        frame.add(engine);
        frame.setSize(800, 600); // Set window size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

// PhysicsObject class representing a 2D object
class PhysicsObject {
    private double x, y; // Position
    private double vx, vy; // Velocity
    private final double radius; // Radius of the object
    private final double mass; // Mass of the object
    private final Color color; // Color for rendering

    public PhysicsObject(double x, double y, double radius, double mass, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.mass = mass;
        this.color = color;
        this.vx = 0;
        this.vy = 0; // Start with no velocity
    }

    // Apply a force to the object
    public void applyForce(double fx, double fy) {
        double ax = fx / mass; // Acceleration in X
        double ay = fy / mass; // Acceleration in Y
        vx += ax;
        vy += ay;
    }

    // Update the position and velocity
    public void update(double deltaTime) {
        x += vx * deltaTime;
        y += vy * deltaTime;
    }

    // Reverse the velocity in the X direction (used for collisions)
    public void reverseVelocityX() {
        vx = -vx * 0.8; // Add some energy loss
    }

    // Reverse the velocity in the Y direction (used for collisions)
    public void reverseVelocityY() {
        vy = -vy * 0.8; // Add some energy loss
    }

    // Draw the object
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));
    }

    // Getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }
}