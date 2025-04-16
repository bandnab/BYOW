package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Core.*;


import java.util.Random;


public class MapGenerator {
    TETile[][] randomMap;
    TERenderer terrain = new TERenderer();
    private long SEED;
    private Random RANDOM;
    private int WIDTH;
    private int HEIGHT;

    public MapGenerator(int height, int width, long seed) {
        HEIGHT = height;
        WIDTH = width;
        RANDOM = new Random(seed);
    }

    private void initialize(TETile[][] map, TETile tile) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                map[i][j] = tile;
            }
        }
    }

    public TETile[][] randoWorldGenerator() {
        randomMap = new TETile[WIDTH][HEIGHT];
        terrain.initialize(WIDTH, HEIGHT);
        initialize(randomMap, Tileset.NOTHING);
        for (int i = 0; i < Math.max(WIDTH, HEIGHT); i++) {
            int posW = RandomUtils.uniform(RANDOM, WIDTH);
            int posH = RandomUtils.uniform(RANDOM, HEIGHT);
            Position start = new Position(posW, posH);
            randomRoomGenerator(randomMap, start, RANDOM);
        }

        for (int i = 0; i < Math.max(WIDTH, HEIGHT); i++) {
            int hallwayW = RandomUtils.uniform(RANDOM, WIDTH);
            int hallwayH = RandomUtils.uniform(RANDOM, WIDTH);
            Position startHallway = new Position(hallwayW, hallwayH);
        }
        wallRemove(randomMap);
        addExitRando(randomMap, RANDOM);
        terrain.renderFrame(randomMap);
        return randomMap;
        }

    private boolean putRoom(TETile[][] room, Position pos, int roomWidth, int roomHeight){
        if (pos.x + roomWidth >= WIDTH){
            if (pos.y + roomHeight >= HEIGHT){
                return false;
            }
        }
        for (int i = pos.x; i < pos.x + roomWidth; i++) {
            for (int j = pos.y; i < pos.y + roomHeight; i++) {
                if (!room[i][j].equals(Tileset.NOTHING)) {
                    return false;
                }
            }

        }
        return true;
    }

    private void randomRoomGenerator(TETile[][] room, Position initial, Random RANDOM){
        int roomH = RandomUtils.uniform(RANDOM, 10, 5);
        int roomW = RandomUtils.uniform(RANDOM, 5, 10);
        if (putRoom(room, initial, roomW, roomH)){
            for (int i = initial.x; i < initial.x + roomW; i++) {
                int yAxis = initial.y;
                room[i][yAxis] = Tileset.WALL;
                room[i][yAxis + roomH - 1] = Tileset.WALL;
            }

            for (int j = initial.y; j < initial.y + roomH; j++) {
                int xAxis = initial.x;
                room[initial.x][j] = Tileset.WALL;
                room[initial.x + roomW][j] = Tileset.WALL;
            }

            for (int i = initial.x + 1; i < initial.x + roomW - 1; i++) {
                for (int j = initial.y + 1; i < initial.y + roomH - 1; i++) {
                    room[i][j] = Tileset.FLOOR;
                }
            }

        }
    }

    private void randomHallwayGenerator(TETile[][] hallway, Position initial, Random RANDOM) {
        while (!(hallway[initial.x][initial.y].equals(Tileset.WALL)) || initial.x < 3 || initial.y < 3 || initial.x > WIDTH - 3 ||
                initial.y > HEIGHT - 3) {
            initial = new Position(RandomUtils.uniform(RANDOM, WIDTH), RandomUtils.uniform(RANDOM, HEIGHT));
            if (initial.x < 3 || initial.y < 3 || initial.x > WIDTH - 3 || initial.y > HEIGHT - 3) {
                continue;
            }
            if (!putHallway(hallway, initial)) {
                continue;
            }

            HallwayPosition direction = hallwayDirection(hallway, initial);
            switch(direction) {
                case UP: {
                    while (initial.y < HEIGHT - 2 && !isConnected(hallway, initial, HallwayPosition.UP)) {
                        hallway[initial.x - 1][initial.y] = Tileset.WALL;
                        hallway[initial.x + 1][initial.y] = Tileset.WALL;
                        hallway[initial.x][initial.y] = Tileset.FLOOR;
                        initial.y += 1;
                    }
                    hallway[initial.x - 1][initial.y] = Tileset.WALL;
                    hallway[initial.x + 1][initial.y] = Tileset.WALL;
                    hallway[initial.x][initial.y] = Tileset.WALL;
                    break;
                }
                case DOWN: {
                    while (initial.y > 1 && !isConnected(hallway, initial, HallwayPosition.DOWN)) {
                        hallway[initial.x - 1][initial.y] = Tileset.WALL;
                        hallway[initial.x + 1][initial.y] = Tileset.WALL;
                        hallway[initial.x][initial.y] = Tileset.FLOOR;
                        initial.y -= 1;
                    }
                    hallway[initial.x - 1][initial.y] = Tileset.WALL;
                    hallway[initial.x + 1][initial.y] = Tileset.WALL;
                    hallway[initial.x][initial.y] = Tileset.WALL;
                    break;
                }
                case LEFT: {
                    while (initial.x > 1 && !isConnected(hallway, initial, HallwayPosition.LEFT)) {
                        hallway[initial.x][initial.y - 1] = Tileset.WALL;
                        hallway[initial.x][initial.y + 1] = Tileset.WALL;
                        hallway[initial.x][initial.y] = Tileset.FLOOR;
                        initial.x -= 1;
                    }
                    hallway[initial.x][initial.y + 1] = Tileset.WALL;
                    hallway[initial.x][initial.y - 1] = Tileset.WALL;
                    hallway[initial.x][initial.y] = Tileset.WALL;
                    break;
                }
                case RIGHT: {
                    while (initial.x < WIDTH - 2 && !isConnected(hallway, initial, HallwayPosition.RIGHT)) {
                        hallway[initial.x][initial.y - 1] = Tileset.WALL;
                        hallway[initial.x][initial.y + 1] = Tileset.WALL;
                        hallway[initial.x][initial.y] = Tileset.FLOOR;
                        initial.x += 1;
                    }
                    hallway[initial.x][initial.y + 1] = Tileset.WALL;
                    hallway[initial.x][initial.y - 1] = Tileset.WALL;
                    hallway[initial.x][initial.y] = Tileset.WALL;
                    break;
                }

            }


        }


    }

    private boolean putHallway (TETile[][] hallway, Position pos) {
        int xPlus1 = pos.x + 1;
        int xMinus1 = pos.x - 1;
        int yPlus1 = pos.y + 1;
        int yMinus1 = pos.y - 1;
        if (hallway[xPlus1][pos.y].equals(Tileset.FLOOR) || hallway[xMinus1][pos.y].equals((Tileset.FLOOR)) ||
                hallway[pos.x][yPlus1].equals(Tileset.FLOOR) || hallway[pos.x][yMinus1].equals(Tileset.FLOOR)) {
            return true;
        }
        return false;
    }

    private HallwayDirection hallwayDirection(TETile[][] world, Position p) {
        HallwayDirection direction = HallwayDirection.NULL;
        if(world[p.x][p.y - 1].equals(Tileset.FLOOR)) {
            direction = HallwayDirection.UP;
        }
        if(world[p.x][p.y + 1].equals(Tileset.FLOOR)) {
            direction = HallwayDirection.DOWN;
        }
        if(world[p.x + 1][p.y].equals(Tileset.FLOOR)) {
            direction = HallwayDirection.LEFT;
        }
        if(world[p.x - 1][p.y].equals(Tileset.FLOOR)) {
            direction =  HallwayDirection.RIGHT;
        }
        return direction;
    }

    private boolean isConnected(TETile[][] world, Position p, HallwayDirection direction) {
        boolean connected = false;
        switch (direction) {
            case UP: {
                if(world[p.x - 1][p.y].equals(Tileset.FLOOR) || world[p.x + 1][p.y].equals(Tileset.FLOOR) ||
                        world[p.x][p.y + 1].equals(Tileset.FLOOR)) {
                    connected = true;
                }
                break;
            }
            case DOWN: {
                if(world[p.x - 1][p.y].equals(Tileset.FLOOR) || world[p.x + 1][p.y].equals(Tileset.FLOOR) ||
                        world[p.x][p.y - 1].equals(Tileset.FLOOR)) {
                    connected = true;
                }
                break;
            }
            case LEFT: {
                if(world[p.x][p.y - 1].equals(Tileset.FLOOR) || world[p.x][p.y + 1].equals(Tileset.FLOOR) ||
                        world[p.x - 1][p.y].equals(Tileset.FLOOR)) {
                    connected = true;
                }
                break;
            }
            case RIGHT: {
                if(world[p.x][p.y - 1].equals(Tileset.FLOOR) || world[p.x][p.y + 1].equals(Tileset.FLOOR) ||
                        world[p.x + 1][p.y].equals(Tileset.FLOOR)) {
                    connected = true;
                }
                break;
            }
        }
        return connected;
    }

    private void removeWall(TETile[][] world) {

        for(int i = 2; i < WIDTH - 2; i++) {
            for(int j = 2; j < HEIGHT - 2; j++) {
                Position currPos = new Position(i, j);
                if(needRemoval(world, currPos)) {
                    world[currPos.x][currPos.y] = Tileset.FLOOR;
                }
            }
        }
        for(int i = 2; i < WIDTH - 2; i++) {
            for(int j = 2; j < HEIGHT - 2; j++) {
                Position currPos = new Position(i, j);
                if(wallWithOpenHallways(world, currPos)) {
                    world[currPos.x][currPos.y] = Tileset.FLOOR;
                }
            }
        }
    }

    private boolean needRemoval(TETile[][] world, Position p) {
        if((world[p.x + 1][p.y].equals(Tileset.FLOOR) && world[p.x - 1][p.y].equals(Tileset.FLOOR) &&
                world[p.x][p.y - 1].equals(Tileset.WALL) && world[p.x][p.y + 1].equals(Tileset.WALL)) ||
                (world[p.x + 1][p.y].equals(Tileset.WALL) && world[p.x - 1][p.y].equals(Tileset.WALL) &&
                        world[p.x][p.y - 1].equals(Tileset.FLOOR) && world[p.x][p.y + 1].equals(Tileset.FLOOR))) {
            return true;
        } else {
            return false;
        }
    }

    private boolean wallWithOpenHallways(TETile[][] world, Position p) {
        if(world[p.x][p.y].equals(Tileset.FLOOR)) return false;
        int numOfFloors = 0;
        if(world[p.x + 1][p.y].equals(Tileset.FLOOR)) {
            numOfFloors += 1;
        }
        if(world[p.x - 1][p.y].equals(Tileset.FLOOR)) {
            numOfFloors += 1;
        }
        if(world[p.x][p.y + 1].equals(Tileset.FLOOR)) {
            numOfFloors += 1;
        }
        if(world[p.x][p.y - 1].equals(Tileset.FLOOR)) {
            numOfFloors += 1;
        }
        if(numOfFloors >= 3) return true;
        return false;

    }

    private void addRandomExit(TETile[][] world, Random RANDOM) {
        Position p = new Position(RandomUtils.uniform(RANDOM, 3, WIDTH - 3), RandomUtils.uniform(RANDOM, 3, HEIGHT - 3));
        if(world[p.x][p.y].equals(Tileset.NOTHING)) {
            p = new Position(RandomUtils.uniform(RANDOM, 3, WIDTH - 3), RandomUtils.uniform(RANDOM, 3, HEIGHT - 3));
        }
        world[p.x][p.y] = Tileset.UNLOCKED_DOOR;
    }

    public Position addRandomAvatar(TETile[][] world, Random RANDOM) {
        Position p = new Position(RandomUtils.uniform(RANDOM, 3, WIDTH - 3), RandomUtils.uniform(RANDOM, 3, HEIGHT - 3));
        if(!world[p.x][p.y].equals(Tileset.FLOOR)) {
            p = new Position(RandomUtils.uniform(RANDOM, 3, WIDTH - 3), RandomUtils.uniform(RANDOM, 3, HEIGHT - 3));
        }
        world[p.x][p.y] = Tileset.AVATAR;
        return p;
    }



}











}

