namespace ElevatorX
{
    public class Startup
    {
        public static void Main(string[] args)
        {
            for (int i = 0; i < 50; i++)
            {
                for (int j = 0; j < i; j++)
                {
                    Console.Write("=");
                }
                Console.WriteLine();
                Thread.Sleep(20);
            }
        }
    }
}