package cw;

public class HexCoord
{
    public int x, y, z;

    public HexCoord(int a, int b, int c)
    {
        x = a;
        y = b;
        z = c;
    }

    public String buildStr()
    {
        return x + "," + y + "," + z;
    }

    public HexCoord[] adjacent()
    {
        return new HexCoord[]{
        new HexCoord(x + 1, y - 1, z),
        new HexCoord(x + 1, y, z - 1),
        new HexCoord(x - 1, y, z + 1),
        new HexCoord(x - 1, y + 1, z),
        new HexCoord(x, y + 1, z - 1),
        new HexCoord(x, y - 1, z + 1)};
    }

    public boolean is(HexCoord h)
    {
        return x == h.x && y == h.y && z == h.z;
    }
}
