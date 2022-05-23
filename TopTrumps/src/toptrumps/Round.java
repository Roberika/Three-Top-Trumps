/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package toptrumps;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JOptionPane;


/**
 *
 * @author User
 */
public class Round extends javax.swing.JFrame {
    
    public int playerAmount;      //jumlah pemain manusia
    public int cpuAmount;         //jumplah cpu
    public int target;            //jumlah kartu untuk menang
    public Deck mainDeck;         //deck utama asal kartu yang digunakan
    public Player[] players;      //array pemain
    public Board board;           //tumpukan kartu ditengah
    public int dropTurn;          //giliran player mana untuk meletakkan kartu pertama
    public int curTurn;           //giliran player setelah peletak pertama
    public int turnCount;         //giliran ke berapa sekarang
    public int contestedAttribute;//atribut apa yang sedang dibandingkan
    public String[] attributes;   //list nama atribut kartu

    /**
     * Creates new form Round
     */
    public Round() {
        this(1);
    }

    public Round(int computers) {
        this(computers, Main.mn.deck);
    }

    public Round(int computers, Deck deck) {
        initComponents();
        this.setLocation(Main.mn.getLocation());
        this.playerAmount = 1;
        this.cpuAmount = computers;
        this.target = 50;
        this.mainDeck = deck;
        this.players = new Player[this.playerAmount + this.cpuAmount];
        for (Player x : this.players) {
            x = new Player();
        }
        this.board = new Board();
        this.dropTurn = 0;
        this.curTurn = 0;
        this.turnCount = 1;
        this.contestedAttribute = -1;
        this.attributes = deck.getAttributeNames();
        for(int j = 0; j < 4; j++){
            ((javax.swing.JLabel)findComponentByName(this,"lblAttribute0"+j)).setText(attributes[j]);
        }
        for(int i = 1; i < 4; i++){
            for(int j = 0; j < 4; j++){
                ((javax.swing.JLabel)findComponentByName(this,"lblAttribute"+i+""+j)).setText(attributes[j]);
            }
        }
        for(int i = cpuAmount; i < 3; i++){
            ((javax.swing.JPanel)findComponentByName(this,"cpu"+i)).setVisible(false);
            ((javax.swing.JPanel)findComponentByName(this,"cpu"+i)).setEnabled(false);
        }
        refresh();
    }
    
    public void turn() {
        if (!players[curTurn].isHuman()) {
            if (curTurn == dropTurn) {
                //players[curTurn].autoDrop();
            } else {
                //players[curTurn].autoAdd();
            }
            endTurn();
        }
    }

    public void endTurn() {
        players[curTurn].draw();
        refresh();
        turnCount++;
        curTurn++;
        if (curTurn == dropTurn) {
            tally();
            dropTurn++;
            curTurn++;
        }
        turn();
    }

    //update layar
    public void refresh() {
        this.lblTurnCount.setText("Turn"+turnCount);
        this.lblPlayerDeck.setText(players[0].getDeck().getSize()+"");
        this.lblContestedAttribute.setText(attributes[contestedAttribute]+" ["+contestedAttribute+"]");
        for(int i = 0; i < this.cpuAmount; i++){
            if(players[i+1].getDeck().getSize()<3){
                if(players[i+1].getDeck().getSize()<=0){
                    continue;
                }
                for(int j = players[i+1].getDeck().getSize(); j < 3; j++){
                    ((javax.swing.JPanel)findComponentByName(this,"cardCPU"+i+""+j)).setVisible(false);
                    ((javax.swing.JPanel)findComponentByName(this,"cardCPU"+i+""+j)).setEnabled(false);
                }
            }
            ((javax.swing.JLabel)findComponentByName(this,"lblCPUDeck"+i)).setText(players[i+1].getDeck().getSize()+"");
        }
        if(board.top()==null){
            ((javax.swing.JLabel)findComponentByName(this,"image0")).setIcon(null);
            ((javax.swing.JButton)findComponentByName(this,"btnCard0")).setText("");
            for(int j = 0; j < 4; j++){
                ((javax.swing.JLabel)findComponentByName(this,"lblValue0"+j)).setText("-");
            }
        } else {
            ((javax.swing.JLabel)findComponentByName(this,"image0")).setIcon(board.top().getIcon());
            ((javax.swing.JButton)findComponentByName(this,"btnCard0")).setText(board.top().getCardName());
            for(int j = 0; j < 4; j++){
                ((javax.swing.JLabel)findComponentByName(this,"lblValue0"+j)).setText(board.top().getValue(j)+"");
            }
        }
        for(int i = 1; i < players[0].getHand().getSize(); i++){
            Card card = players[0].getHand().get(i);
            if(card==null){
                ((javax.swing.JLabel)findComponentByName(this,"image"+i)).setIcon(null);
                ((javax.swing.JButton)findComponentByName(this,"btnCard"+i)).setText("");
                for(int j = 0; j < 4; j++){
                    ((javax.swing.JLabel)findComponentByName(this,"lblValue"+i+""+j)).setText("-");
                }
                continue;
            }
            ((javax.swing.JLabel)findComponentByName(this,"image"+i)).setIcon(card.getIcon());
            ((javax.swing.JButton)findComponentByName(this,"btnCard"+i)).setText(card.getCardName());
            for(int j = 0; j < 4; j++){
                ((javax.swing.JLabel)findComponentByName(this,"lblValue"+i+""+j)).setText(card.getValue(j)+"");
            }
        }
    }
    
    //cari kartu yang menang dan pemain yang menaruhnya, lalu beri semua kartu
    //di drop ke pemain itu
    public void tally() {
        int winner = board.getWinner();
        if(winner!=-1){//kalau tidak seri
            players[winner].getDeck().add(board.toArray());
            board.clear();
            if(players[winner].getDeck().getSize()>target){
                exit();
            }
        }
    }

    public void exit() {
        if(players[0].getDeck().getSize()>target){
            Main.mn.wins++;
        }
        Main.mn.rounds++;
        Main.mn.setLocation(this.getLocation());
        Main.mn.setVisible(true);
        this.dispose();
    }

    public void btnCard(int index) {
        if (dropTurn == curTurn) {
            new Select().setVisible(true);
        } else {
            board.add(players[curTurn].getHand().discard(index));
            endTurn();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        singleplayer = new javax.swing.JPanel();
        card1 = new javax.swing.JPanel();
        image1 = new javax.swing.JLabel();
        btnCard1 = new javax.swing.JButton();
        lblAttribute10 = new javax.swing.JLabel();
        lblAttribute11 = new javax.swing.JLabel();
        lblAttribute12 = new javax.swing.JLabel();
        lblAttribute13 = new javax.swing.JLabel();
        lblValue10 = new javax.swing.JLabel();
        lblValue11 = new javax.swing.JLabel();
        lblValue12 = new javax.swing.JLabel();
        lblValue13 = new javax.swing.JLabel();
        btnBurn = new javax.swing.JButton();
        lblPlayerDeck = new javax.swing.JLabel();
        card2 = new javax.swing.JPanel();
        image2 = new javax.swing.JLabel();
        btnCard2 = new javax.swing.JButton();
        lblAttribute20 = new javax.swing.JLabel();
        lblAttribute21 = new javax.swing.JLabel();
        lblAttribute22 = new javax.swing.JLabel();
        lblAttribute23 = new javax.swing.JLabel();
        lblValue20 = new javax.swing.JLabel();
        lblValue21 = new javax.swing.JLabel();
        lblValue22 = new javax.swing.JLabel();
        lblValue23 = new javax.swing.JLabel();
        card3 = new javax.swing.JPanel();
        image3 = new javax.swing.JLabel();
        btnCard3 = new javax.swing.JButton();
        lblAttribute30 = new javax.swing.JLabel();
        lblAttribute31 = new javax.swing.JLabel();
        lblAttribute32 = new javax.swing.JLabel();
        lblAttribute33 = new javax.swing.JLabel();
        lblValue30 = new javax.swing.JLabel();
        lblValue31 = new javax.swing.JLabel();
        lblValue32 = new javax.swing.JLabel();
        lblValue33 = new javax.swing.JLabel();
        card0 = new javax.swing.JPanel();
        image0 = new javax.swing.JLabel();
        btnCard0 = new javax.swing.JButton();
        lblAttribute00 = new javax.swing.JLabel();
        lblAttribute01 = new javax.swing.JLabel();
        lblAttribute02 = new javax.swing.JLabel();
        lblAttribute03 = new javax.swing.JLabel();
        lblValue00 = new javax.swing.JLabel();
        lblValue01 = new javax.swing.JLabel();
        lblValue02 = new javax.swing.JLabel();
        lblValue03 = new javax.swing.JLabel();
        cpu0 = new javax.swing.JPanel();
        lblCPUName0 = new javax.swing.JLabel();
        lblCPUDeck0 = new javax.swing.JLabel();
        cardCPU00 = new javax.swing.JPanel();
        cardCPU01 = new javax.swing.JPanel();
        cardCPU02 = new javax.swing.JPanel();
        cpu1 = new javax.swing.JPanel();
        lblCPUName1 = new javax.swing.JLabel();
        lblCPUDeck1 = new javax.swing.JLabel();
        cardCPU10 = new javax.swing.JPanel();
        cardCPU11 = new javax.swing.JPanel();
        cardCPU12 = new javax.swing.JPanel();
        cpu2 = new javax.swing.JPanel();
        lblCPUName2 = new javax.swing.JLabel();
        lblCPUDeck2 = new javax.swing.JLabel();
        cardCPU20 = new javax.swing.JPanel();
        cardCPU21 = new javax.swing.JPanel();
        cardCPU22 = new javax.swing.JPanel();
        lblTurnCount = new javax.swing.JLabel();
        lblContestedAttribute = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        singleplayer.setName(""); // NOI18N

        card1.setBackground(new java.awt.Color(255, 255, 255));

        image1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        image1.setText("image");

        btnCard1.setBackground(new java.awt.Color(204, 204, 204));
        btnCard1.setText("Wizard");
        btnCard1.setBorder(null);
        btnCard1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCard1ActionPerformed(evt);
            }
        });

        lblAttribute10.setText("Strength");

        lblAttribute11.setText("Intelligence");

        lblAttribute12.setText("Agility");

        lblAttribute13.setText("Luck");

        lblValue10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblValue10.setText("2");

        lblValue11.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblValue11.setText("9");

        lblValue12.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblValue12.setText("1");

        lblValue13.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblValue13.setText("6");

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(image1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnCard1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAttribute11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAttribute12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAttribute13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAttribute10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblValue10, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addComponent(lblValue11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblValue13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblValue12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        card1Layout.setVerticalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card1Layout.createSequentialGroup()
                .addComponent(image1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCard1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAttribute10)
                    .addComponent(lblValue10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAttribute11)
                    .addComponent(lblValue11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAttribute12)
                    .addComponent(lblValue12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAttribute13)
                    .addComponent(lblValue13)))
        );

        btnBurn.setText("Burn Cards");
        btnBurn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBurnActionPerformed(evt);
            }
        });

        lblPlayerDeck.setBackground(new java.awt.Color(204, 204, 204));
        lblPlayerDeck.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        lblPlayerDeck.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPlayerDeck.setText("100");
        lblPlayerDeck.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        card2.setBackground(new java.awt.Color(255, 255, 255));

        image2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        image2.setText("image");

        btnCard2.setBackground(new java.awt.Color(204, 204, 204));
        btnCard2.setText("Archer");
        btnCard2.setBorder(null);
        btnCard2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCard2ActionPerformed(evt);
            }
        });

        lblAttribute20.setText("Strength");

        lblAttribute21.setText("Intelligence");

        lblAttribute22.setText("Agility");

        lblAttribute23.setText("Luck");

        lblValue20.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblValue20.setText("4");

        lblValue21.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblValue21.setText("4");

        lblValue22.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblValue22.setText("7");

        lblValue23.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblValue23.setText("6");

        javax.swing.GroupLayout card2Layout = new javax.swing.GroupLayout(card2);
        card2.setLayout(card2Layout);
        card2Layout.setHorizontalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(image2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnCard2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAttribute21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAttribute22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAttribute23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAttribute20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblValue20, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addComponent(lblValue21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblValue23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblValue22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        card2Layout.setVerticalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card2Layout.createSequentialGroup()
                .addComponent(image2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCard2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAttribute20)
                    .addComponent(lblValue20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAttribute21)
                    .addComponent(lblValue21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAttribute22)
                    .addComponent(lblValue22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAttribute23)
                    .addComponent(lblValue23)))
        );

        card3.setBackground(new java.awt.Color(255, 255, 255));

        image3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        image3.setText("image");

        btnCard3.setBackground(new java.awt.Color(204, 204, 204));
        btnCard3.setText("Knight");
        btnCard3.setBorder(null);
        btnCard3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCard3ActionPerformed(evt);
            }
        });

        lblAttribute30.setText("Strength");

        lblAttribute31.setText("Intelligence");

        lblAttribute32.setText("Agility");

        lblAttribute33.setText("Luck");

        lblValue30.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblValue30.setText("7");

        lblValue31.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblValue31.setText("4");

        lblValue32.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblValue32.setText("3");

        lblValue33.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblValue33.setText("2");

        javax.swing.GroupLayout card3Layout = new javax.swing.GroupLayout(card3);
        card3.setLayout(card3Layout);
        card3Layout.setHorizontalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(image3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnCard3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAttribute31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAttribute32, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAttribute33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAttribute30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblValue30, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addComponent(lblValue31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblValue33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblValue32, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        card3Layout.setVerticalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card3Layout.createSequentialGroup()
                .addComponent(image3, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCard3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAttribute30)
                    .addComponent(lblValue30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAttribute31)
                    .addComponent(lblValue31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAttribute32)
                    .addComponent(lblValue32))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAttribute33)
                    .addComponent(lblValue33)))
        );

        card0.setBackground(new java.awt.Color(255, 255, 255));

        image0.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        image0.setText("image");

        btnCard0.setBackground(new java.awt.Color(204, 204, 204));
        btnCard0.setText("Wizard");
        btnCard0.setBorder(null);
        btnCard0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCard0ActionPerformed(evt);
            }
        });

        lblAttribute00.setText("Strength");

        lblAttribute01.setText("Intelligence");

        lblAttribute02.setText("Agility");

        lblAttribute03.setText("Luck");

        lblValue00.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblValue00.setText("2");

        lblValue01.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblValue01.setText("9");

        lblValue02.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblValue02.setText("1");

        lblValue03.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblValue03.setText("6");

        javax.swing.GroupLayout card0Layout = new javax.swing.GroupLayout(card0);
        card0.setLayout(card0Layout);
        card0Layout.setHorizontalGroup(
            card0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(image0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnCard0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, card0Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(card0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAttribute01, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAttribute02, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAttribute03, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAttribute00, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblValue00, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addComponent(lblValue01, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblValue03, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblValue02, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        card0Layout.setVerticalGroup(
            card0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card0Layout.createSequentialGroup()
                .addComponent(image0, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCard0)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAttribute00)
                    .addComponent(lblValue00))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAttribute01)
                    .addComponent(lblValue01))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAttribute02)
                    .addComponent(lblValue02))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(card0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAttribute03)
                    .addComponent(lblValue03)))
        );

        cpu0.setBackground(new java.awt.Color(255, 255, 255));

        lblCPUName0.setText("CPU 1");

        lblCPUDeck0.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCPUDeck0.setText("100");

        cardCPU00.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout cardCPU00Layout = new javax.swing.GroupLayout(cardCPU00);
        cardCPU00.setLayout(cardCPU00Layout);
        cardCPU00Layout.setHorizontalGroup(
            cardCPU00Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );
        cardCPU00Layout.setVerticalGroup(
            cardCPU00Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        cardCPU01.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout cardCPU01Layout = new javax.swing.GroupLayout(cardCPU01);
        cardCPU01.setLayout(cardCPU01Layout);
        cardCPU01Layout.setHorizontalGroup(
            cardCPU01Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );
        cardCPU01Layout.setVerticalGroup(
            cardCPU01Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        cardCPU02.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout cardCPU02Layout = new javax.swing.GroupLayout(cardCPU02);
        cardCPU02.setLayout(cardCPU02Layout);
        cardCPU02Layout.setHorizontalGroup(
            cardCPU02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );
        cardCPU02Layout.setVerticalGroup(
            cardCPU02Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout cpu0Layout = new javax.swing.GroupLayout(cpu0);
        cpu0.setLayout(cpu0Layout);
        cpu0Layout.setHorizontalGroup(
            cpu0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cpu0Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cpu0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cpu0Layout.createSequentialGroup()
                        .addComponent(cardCPU00, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cardCPU01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cardCPU02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(cpu0Layout.createSequentialGroup()
                        .addComponent(lblCPUName0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCPUDeck0, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        cpu0Layout.setVerticalGroup(
            cpu0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cpu0Layout.createSequentialGroup()
                .addGroup(cpu0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCPUName0)
                    .addComponent(lblCPUDeck0))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cpu0Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cardCPU00, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardCPU01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardCPU02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cpu1.setBackground(new java.awt.Color(255, 255, 255));

        lblCPUName1.setText("CPU 2");

        lblCPUDeck1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCPUDeck1.setText("100");

        cardCPU10.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout cardCPU10Layout = new javax.swing.GroupLayout(cardCPU10);
        cardCPU10.setLayout(cardCPU10Layout);
        cardCPU10Layout.setHorizontalGroup(
            cardCPU10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );
        cardCPU10Layout.setVerticalGroup(
            cardCPU10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        cardCPU11.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout cardCPU11Layout = new javax.swing.GroupLayout(cardCPU11);
        cardCPU11.setLayout(cardCPU11Layout);
        cardCPU11Layout.setHorizontalGroup(
            cardCPU11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );
        cardCPU11Layout.setVerticalGroup(
            cardCPU11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        cardCPU12.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout cardCPU12Layout = new javax.swing.GroupLayout(cardCPU12);
        cardCPU12.setLayout(cardCPU12Layout);
        cardCPU12Layout.setHorizontalGroup(
            cardCPU12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );
        cardCPU12Layout.setVerticalGroup(
            cardCPU12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout cpu1Layout = new javax.swing.GroupLayout(cpu1);
        cpu1.setLayout(cpu1Layout);
        cpu1Layout.setHorizontalGroup(
            cpu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cpu1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cpu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cpu1Layout.createSequentialGroup()
                        .addComponent(cardCPU10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cardCPU11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cardCPU12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(cpu1Layout.createSequentialGroup()
                        .addComponent(lblCPUName1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCPUDeck1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        cpu1Layout.setVerticalGroup(
            cpu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cpu1Layout.createSequentialGroup()
                .addGroup(cpu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCPUName1)
                    .addComponent(lblCPUDeck1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cpu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cardCPU10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardCPU11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardCPU12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cpu2.setBackground(new java.awt.Color(255, 255, 255));

        lblCPUName2.setText("CPU 3");

        lblCPUDeck2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCPUDeck2.setText("100");

        cardCPU20.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout cardCPU20Layout = new javax.swing.GroupLayout(cardCPU20);
        cardCPU20.setLayout(cardCPU20Layout);
        cardCPU20Layout.setHorizontalGroup(
            cardCPU20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );
        cardCPU20Layout.setVerticalGroup(
            cardCPU20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        cardCPU21.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout cardCPU21Layout = new javax.swing.GroupLayout(cardCPU21);
        cardCPU21.setLayout(cardCPU21Layout);
        cardCPU21Layout.setHorizontalGroup(
            cardCPU21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );
        cardCPU21Layout.setVerticalGroup(
            cardCPU21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        cardCPU22.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout cardCPU22Layout = new javax.swing.GroupLayout(cardCPU22);
        cardCPU22.setLayout(cardCPU22Layout);
        cardCPU22Layout.setHorizontalGroup(
            cardCPU22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );
        cardCPU22Layout.setVerticalGroup(
            cardCPU22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout cpu2Layout = new javax.swing.GroupLayout(cpu2);
        cpu2.setLayout(cpu2Layout);
        cpu2Layout.setHorizontalGroup(
            cpu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cpu2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cpu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cpu2Layout.createSequentialGroup()
                        .addComponent(cardCPU20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cardCPU21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cardCPU22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(cpu2Layout.createSequentialGroup()
                        .addComponent(lblCPUName2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCPUDeck2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        cpu2Layout.setVerticalGroup(
            cpu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cpu2Layout.createSequentialGroup()
                .addGroup(cpu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCPUName2)
                    .addComponent(lblCPUDeck2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cpu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cardCPU20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardCPU21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardCPU22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblTurnCount.setText("Turn 15");

        lblContestedAttribute.setText("Luck [4]");

        javax.swing.GroupLayout singleplayerLayout = new javax.swing.GroupLayout(singleplayer);
        singleplayer.setLayout(singleplayerLayout);
        singleplayerLayout.setHorizontalGroup(
            singleplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(singleplayerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(singleplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(singleplayerLayout.createSequentialGroup()
                        .addGroup(singleplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblPlayerDeck, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnBurn, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(card3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(singleplayerLayout.createSequentialGroup()
                        .addComponent(card0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(singleplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(singleplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(singleplayerLayout.createSequentialGroup()
                                    .addComponent(cpu0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(cpu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(cpu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(lblTurnCount, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(lblContestedAttribute, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        singleplayerLayout.setVerticalGroup(
            singleplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(singleplayerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(singleplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(singleplayerLayout.createSequentialGroup()
                        .addComponent(cpu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblTurnCount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblContestedAttribute))
                    .addComponent(cpu0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cpu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(singleplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(singleplayerLayout.createSequentialGroup()
                        .addComponent(lblPlayerDeck, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBurn))
                    .addGroup(singleplayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(card3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(singleplayer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(singleplayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (JOptionPane.showConfirmDialog(null, "Do you want to end the round?", "Quit",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            exit();
        }
    }//GEN-LAST:event_formWindowClosing

    private void btnCard0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCard0ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCard0ActionPerformed

    private void btnCard3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCard3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCard3ActionPerformed

    private void btnCard2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCard2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCard2ActionPerformed

    private void btnBurnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBurnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBurnActionPerformed

    private void btnCard1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCard1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCard1ActionPerformed
    
    private static Component findComponentByName(Component component, String componentName) {
        if(component==null ){
            return null;
        }
        if(component.getName()!=null && component.getName().equalsIgnoreCase(componentName)) {
            return component;
        }
        if((component instanceof Container)){       
            Component[] children = ((Container)component).getComponents();
            for(Component child : children){
                Component found = findComponentByName(child,componentName);
                if(found != null){
                    return found;
                }
            }
        }
        return null;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Round.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Round.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Round.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Round.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Round().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBurn;
    private javax.swing.JButton btnCard0;
    private javax.swing.JButton btnCard1;
    private javax.swing.JButton btnCard2;
    private javax.swing.JButton btnCard3;
    private javax.swing.JPanel card0;
    private javax.swing.JPanel card1;
    private javax.swing.JPanel card2;
    private javax.swing.JPanel card3;
    private javax.swing.JPanel cardCPU00;
    private javax.swing.JPanel cardCPU01;
    private javax.swing.JPanel cardCPU02;
    private javax.swing.JPanel cardCPU10;
    private javax.swing.JPanel cardCPU11;
    private javax.swing.JPanel cardCPU12;
    private javax.swing.JPanel cardCPU20;
    private javax.swing.JPanel cardCPU21;
    private javax.swing.JPanel cardCPU22;
    private javax.swing.JPanel cpu0;
    private javax.swing.JPanel cpu1;
    private javax.swing.JPanel cpu2;
    private javax.swing.JLabel image0;
    private javax.swing.JLabel image1;
    private javax.swing.JLabel image2;
    private javax.swing.JLabel image3;
    private javax.swing.JLabel lblAttribute00;
    private javax.swing.JLabel lblAttribute01;
    private javax.swing.JLabel lblAttribute02;
    private javax.swing.JLabel lblAttribute03;
    private javax.swing.JLabel lblAttribute10;
    private javax.swing.JLabel lblAttribute11;
    private javax.swing.JLabel lblAttribute12;
    private javax.swing.JLabel lblAttribute13;
    private javax.swing.JLabel lblAttribute20;
    private javax.swing.JLabel lblAttribute21;
    private javax.swing.JLabel lblAttribute22;
    private javax.swing.JLabel lblAttribute23;
    private javax.swing.JLabel lblAttribute30;
    private javax.swing.JLabel lblAttribute31;
    private javax.swing.JLabel lblAttribute32;
    private javax.swing.JLabel lblAttribute33;
    private javax.swing.JLabel lblCPUDeck0;
    private javax.swing.JLabel lblCPUDeck1;
    private javax.swing.JLabel lblCPUDeck2;
    private javax.swing.JLabel lblCPUName0;
    private javax.swing.JLabel lblCPUName1;
    private javax.swing.JLabel lblCPUName2;
    private javax.swing.JLabel lblContestedAttribute;
    private javax.swing.JLabel lblPlayerDeck;
    private javax.swing.JLabel lblTurnCount;
    private javax.swing.JLabel lblValue00;
    private javax.swing.JLabel lblValue01;
    private javax.swing.JLabel lblValue02;
    private javax.swing.JLabel lblValue03;
    private javax.swing.JLabel lblValue10;
    private javax.swing.JLabel lblValue11;
    private javax.swing.JLabel lblValue12;
    private javax.swing.JLabel lblValue13;
    private javax.swing.JLabel lblValue20;
    private javax.swing.JLabel lblValue21;
    private javax.swing.JLabel lblValue22;
    private javax.swing.JLabel lblValue23;
    private javax.swing.JLabel lblValue30;
    private javax.swing.JLabel lblValue31;
    private javax.swing.JLabel lblValue32;
    private javax.swing.JLabel lblValue33;
    private javax.swing.JPanel singleplayer;
    // End of variables declaration//GEN-END:variables
}
