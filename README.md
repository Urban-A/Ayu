# Ayu
Abstraktna strateška igra, ki se igra na križiščih mreže z lihim številom križišč (sodo število kvadratov)

## Konceptualizacija
### Dinamika
- Igralca si izmenjujeta poteze pri premikanju pripadajočih figur

### Mehanika
- Pramikaje figur na sosednja mesta
- Pramikanje figur v skupini
### Elementi
- Črne figure
- Bele figure
- Igralno polje

## Začetno stanje
Belele in črne figure so po križiščih prepleteno razporejene.
Črne figure se postavijo na sodih stolpcih lihih vrstic, bele pa na lihe stolpce sodih vrstic, tako je zapolnjeno pol križišč na mreži.

## Pravila potez
- Sosednjost figur in premikanje je možno tudi poševno
- Figura, ki ni del skupine se lahko premakne na sosednje prazno polje
- Če je figura v skupini se lahko premakne na prazno mesto, ki je sosednje skupini. Število figur združenih v skupino se ne sme spremeniti.
- Vsak premik figure mora zmanjšati razdaljo med premaknjeno figuro in najbližjo figuro, z katero ni v skupini (razdalja je enaka številu premikov med njima)

## Zmaga
Igralec zmaga, če ne more narediti nobenega premika

## Stanje igre
Stanje igralnega polja se bo vodilo z pomočjo 2-dimenzionalnega EnumSet polja, kjer je določena pozicija lahko WHITE, BLACK ali EMPTY, lastnost WHITE/BLACK dobi če je na tem mestu figura pripadajoče barve. Pozicije na katerih so figure imajo lahko še lastnost MOVABLE, ki pove, da je figuro možno premakniti.

    public enum CellState {
      BLACK, WHITE, EMPTY, MOVABLE;

      public static EnumSet<CellState> WH_MOVABLE = EnumSet.of(WHITE, MOVABLE);
      public static EnumSet<CellState> BL_MOVABLE = EnumSet.of(BLACK, MOVABLE);
    }
    public EnumSet<CellState>[][] playingField;

## Viri
O igri http://www.mindsports.nl/index.php/arena/ayu/726-about-ayu  
Pravila http://www.mindsports.nl/index.php/arena/ayu/724-ayu-rules
