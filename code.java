var inquirer = require('inquirer');

var mysql = require('mysql');

var connection = mysql.createConnection({
  host: "localhost",
    
  port: 3306,
    
  user: "root",
});

connection.connect(function(err) {
  if (err) throw err;
  connection.query("SELECT id, product_name, price FROM products", function (err, result, fields) {
    if (err) throw err;
    
    questions();
  });
});
            
function questions() {
  inquirer.prompt([
      {
        name: "id",
        message: "Got some rare things on sale, stranger what wherya lookin at!",
        validate: function(value) {
          if (isNaN(value) === false) {
            return true;
          }
          return false;
        }
      },
      
      {
        name: "quantity",
        message: "What're ya buyin?  How many stranger?"  
//          "UPDATE products SET ? WHERE ?"
      }
  ]).then(function (answers) {
      
    var quantityInput = answers.quantity;
    var idInput = answers.id;
    purchase(idInput, quantityInput);  

});
}

function purchase(id, quantityInput) {
 
    connection.query('SELECT * FROM products WHERE id = ' + id, function(error, response) {
        if (error) { console.log(error) };

        
        if (quantityInput <= response[0].stock_quantity) {
           
            var totalCost = response[0].price * quantityInput;
            
            console.log("\n We have what you need! I'll have your order right out!");
            console.log("Your total cost for " + quantityInput + " " + response[0].product_name + " is $" + totalCost + ". Hehehe Thaank You! \n");
            
            connection.query('UPDATE Products SET stock_quantity = stock_quantity - ' + quantityInput + ' WHERE id = ' + id);
            
            process.exit()
        } else {
            console.log("Don't Have Enough" + response[0].product_name + " Stranger! \n ");
            
            process.exit()
        };
        
    });

};
