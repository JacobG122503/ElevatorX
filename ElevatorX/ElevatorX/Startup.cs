using Figgle;

namespace ElevatorX
{
    public class Startup
    {
        public static void Main(string[] args)
        {
            int nmbFloors;
            int nmbElevators;
            bool confirmed = false;

            Console.WriteLine(FiggleFonts.Banner3D.Render("ElevatorX"));
            Thread.Sleep(1000);

            while (!confirmed)
            {
                Console.WriteLine("How many floors would you like your building to have? ");
                nmbFloors = int.Parse(Console.ReadLine());
                
            }
            



            Console.WriteLine(nmbFloors);
        }
    }
}