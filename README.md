# MarketPlace Plugin

## 📌 About the Plugin

MarketPlace is a plugin for Spigot 1.20.4 servers that allows players to list, sell, and buy items on the market. It has integration with MongoDB to store player data, item listings, and transaction history. As an alternative to MongoDB, MySQL can also be used with a Redis cache system.

## 📅 Deadline
**14 days** (but i finished in 6 days)

## ⚙️ Requirements
### 1️⃣ Database
- Support for **MongoDB** or **MySQL** (with Redis for cache)
- Stores **player data**, **listed items**, and **transaction history**

### 2️⃣ Commands and Permissions
| Command | Description | Permission |
|---------|------------|------------|
| `/sell <price>` | List the item in the player's hand on the market | `marketplace.sell` |
| `/marketplace` | View all items available for purchase | `marketplace.view` |
| `/blackmarket` | Open the black market with random items at 50% off the original price | `marketplace.blackmarket` |
| `/transactions` | View the player's transaction history | `marketplace.history` |

### 3️⃣ Item Selling and Buying
- **Confirmation GUI** before purchasing
- On the **black market**, players buy with a **50% discount** and the seller receives **double the price**
- Full support for **custom items (NBT)**

### 4️⃣ Error Handling
- Clear messages for **incorrect command usage**
- Prevention of **duplicate items (NO DUPE BUGS)**
- Ensures **data consistency** when saving and retrieving information

### 5️⃣ Extra Features
- **Dynamic GUI update**, without reopening the inventory
- **Discord Webhook** to log every purchase made

## 🔧 Installation
1. Download the `.jar` plugin file
2. Place the file in the `/plugins` folder of your Spigot 1.20.4 server
3. Configure the database credentials in the `config.yml` file
4. Restart the server

## 📸 Plugin Screenshots

![Menu](https://i.imgur.com/3C1EwG2.png)
![Menu](https://i.imgur.com/6Td2fdQ.png)
![Menu](https://i.imgur.com/ZB2txq7.png)
![Menu](https://i.imgur.com/Sr1KMNh.png)
![Menu](https://i.imgur.com/2rDguR4.png)

## 📝 Configuration
Example of `config.yml`:
```yaml
mongodb:
  uri: "mongodb://localhost:27017"
  database: "devroom"

messages:
  only-players: "&cOnly players can execute this command."
  no-permission: "&cYou don't have permission to execute this command."
  error: "&cAn error occurred while trying to execute this command, please try again."
  no-money: "&cYou don't have enough money to buy this item."
  buy-item: "&aYou bought from {seller} the item &f{name} &afor &f${price}&a."
  buy-item-seller: "&a{buyer} bought &f{name} &afrom you for &f${price}&a."
  sell-usage: "&cUsage: /sell <price>"
  invalid-price: "&cInvalid price, please enter a valid number."
  hold-item: "&cYou must hold the item you want to sell."
  sell-item: "&aYou listed item &afor sale for &f${price}&a."
config:
  update-blackmarket: 5
discord:
  enabled: true
  webhook: "https://discord.com/api/webhooks/"
  embed:
    username: "Market Announcer"
    title: "New item listed"
    description:
      - "A new item has been listed for sale in the marketplace."
    color: "#FFFFFF"
    footer: "Devroom Market System"
    fields:
      - name: "Item Type"
        value: "{item}"
        inline: true
      - name: "Seller"
        value: "{seller}"
        inline: true
      - name: "Buyer"
        value: "{buyer}"
        inline: true
      - name: "Price"
        value: "$ {price}"
        inline: true
      - name: "Listed At"
        value: "{date}"
        inline: true

menu-config:
  not-paginated:
    slot-default: 0
  back:
    slot: 0
    material: ARROW
    name: "&cBack"
  next:
    slot: 8
    material: ARROW
    name: "&cNext"
menus:
  confirmation:
    row: 3
    title: "You confirm?"
    items:
      # no change this name
      confirm:
        slot: 13
        material: GREEN_DYE
        data: 0
        name: "&aYes, buy it! ({name})"
        lore:
          - "&7Click here to confirm and pay ${price}."
  market:
    row: 5
    title: "Market"
    layout:
      - "XXXXXXXXX"
      - "XOOOOOOOX"
      - "XOOOOOOOX"
      - "XOOOOOOOX"
      - "XXXXXXXXX"
    item-format:
      name: "&eProduct: &f{name}"
      lore:
        - "&7Price: &f{price}"
        - "&7Listed at: &f{listed}"
        - ""
        - "&eClick here to buy."
  blackmarket:
    row: 5
    title: "BlackMarket"
    layout:
      - "XXXXXXXXX"
      - "XOOOOOOOX"
      - "XOOOOOOOX"
      - "XOOOOOOOX"
      - "XXXXXXXXX"
    item-format:
      name: "&eProduct: &f{name}"
      lore:
        - "&7Price: &f{price} &c50% OFF"
        - "&7Listed at: &f{listed}"
        - ""
        - "&eClick here to buy."
  transaction:
    row: 5
    title: "Transactions"
    layout:
      - "XXXXXXXXX"
      - "XOOOOOOOX"
      - "XOOOOOOOX"
      - "XOOOOOOOX"
      - "XXXXXXXXX"
    item-format:
      material: PAPER
      data: 0
      name: "&eTransaction"
      lore:
        - "&7Date: &f{date}"
        - "&7Price: &f{price}"
        - "&7Seller: &f{seller}"
        - "&7Buyer: &f{buyer}"
```

## 📜 License
This project is licensed under the MIT License. Feel free to modify and contribute!

---
