import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;



    public class FlemmeBot extends ListenerAdapter {
    private JDA jda;
    private String channelId;

    public FlemmeBot(String token, String channelId) throws Exception {
        this.channelId = channelId;
        jda = JDABuilder.createDefault(token).addEventListeners(this).build();
        jda.awaitReady();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.getChannel().getId().equals(channelId)) {
            if (event.getMessage().getContentRaw().equals("!difference")) {
                boolean differenceFound = findDifference();

                if (differenceFound) {
                    sendMessage("Une différence a été trouvée !");
                }
            }
        }
    }

    private void sendMessage(String content) {
        MessageChannel channel = jda.getTextChannelById(channelId);
        if (channel != null) {
            channel.sendMessage(content).queue();
        }
    }

        private static boolean fiveSeconds() {
        long currentTime = System.currentTimeMillis();
        return currentTime % 50 == 0;
    }
    public boolean findDifference() {
        try {
            String format = ".png";
            boolean b;
            Robot robot = new Robot();
            BufferedImage oldImage, newImage, capture;
            int width = 48;
            int height = 27;
            oldImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Rectangle screenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            Graphics2D drawOld = oldImage.createGraphics();
            capture = robot.createScreenCapture(screenSize);
            newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D drawNew = newImage.createGraphics();
            drawNew.drawImage(capture, 0, 0, width, height, null);
            drawNew.dispose();
            if (fiveSeconds()) {
                for (int y = 0 ; y < height ; y++) {
                    for (int x = 0 ; x < width ; x++) {
                        if (oldImage.getRGB(x,y) != newImage.getRGB(x,y)) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        break;
                    }
                }
            }

            drawOld.drawImage(capture, 0, 0, width, height, null);
            drawOld.dispose();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        String token = "MTExMDI3ODA3MDYyOTMxODgxOQ.GFVvHg.UPqQ0F_akrrPTH2TLGgKP8U4y_i3B9BZNlzHG4";
        String channelId = "1110277647453388924";
        try {
            FlemmeBot bot = new FlemmeBot(token, channelId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
