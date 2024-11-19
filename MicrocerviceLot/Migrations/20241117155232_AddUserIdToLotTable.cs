using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace MicroserviceLot.Migrations
{
    /// <inheritdoc />
    public partial class AddUserIdToLotTable : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<long>(
                name: "UserId",
                table: "Lot",
                type: "bigint",
                nullable: false,
                defaultValue: 0L);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "UserId",
                table: "Lot");
        }
    }
}
