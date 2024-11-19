
using Microsoft.EntityFrameworkCore;

namespace MicroserviceLot
{
    public class Program
    {
        public static void Main(string[] args)
            {
                var builder = WebApplication.CreateBuilder(args);

                builder.Services.AddDbContext<LotContext>(options =>
                   options.UseSqlServer(builder.Configuration.GetConnectionString("LotContext")));
                // Add services to the container.

                builder.Services.AddControllers();

                var app = builder.Build();

                // Configure the HTTP request pipeline.

                app.UseHttpsRedirection();

                app.UseAuthorization();

                app.MapControllers();

                app.Run();
            }
    }
}

