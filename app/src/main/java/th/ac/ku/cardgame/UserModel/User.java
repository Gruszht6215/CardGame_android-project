package th.ac.ku.cardgame.UserModel;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.gson.annotations.Expose;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import th.ac.ku.cardgame.CardGameSolidity;
import th.ac.ku.cardgame.Shop.CardItem;

public class User {
    @Expose
    String id;
    @Expose
    String key;
    List<Transaction> transactions;
    @Expose
    int using_card_id;
    @Expose
    List<CardItem> cards;
    BigInteger userEth;

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public int getUsing_card_id() {
        return using_card_id;
    }

    public List<CardItem> getCards() {
        return cards;
    }

    public BigInteger getUserEth() {
        return userEth;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setUsing_card_id(int using_card_id) {
        this.using_card_id = using_card_id;
    }

    public void setCards(List<CardItem> cards) {
        this.cards = cards;
    }

    public void setUserEth(BigInteger userEth) {
        this.userEth = userEth;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", using_card_id=" + using_card_id +
                ", userEth=" + userEth +
                '}';
    }

    public void initialPlayerEth() {
        Web3j web3 = Web3j.build(new HttpService("https://goerli.infura.io/v3/f52c5ffe6f3249b3b5c553208d952fa1"));
        Credentials credentials = Credentials.create("405d238d5eb72504ff3f6f52a3ea0df6a27a378eeb5a3985901d06dfe4f6877a");
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        CardGameSolidity cardGameSolidity = CardGameSolidity.load(getKey(), web3, credentials, contractGasProvider);

        cardGameSolidity.retrieve().flowable().subscribeOn(Schedulers.io()).subscribe(new Consumer<BigInteger>() {
            @Override
            public void accept(BigInteger bigInteger) throws Exception {
                Log.i("vac", "initailEth: " + bigInteger);
                setUserEth(bigInteger);
            }
        });
    }

    public void retrievePlayerEth() {
        Web3j web3 = Web3j.build(new HttpService("https://goerli.infura.io/v3/f52c5ffe6f3249b3b5c553208d952fa1"));
        Credentials credentials = Credentials.create("405d238d5eb72504ff3f6f52a3ea0df6a27a378eeb5a3985901d06dfe4f6877a");
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        CardGameSolidity cardGameSolidity = CardGameSolidity.load(getKey(), web3, credentials, contractGasProvider);

        cardGameSolidity.retrieve().flowable().subscribeOn(Schedulers.io()).subscribe(new Consumer<BigInteger>() {
            @Override
            public void accept(BigInteger bigInteger) throws Exception {
                Log.i("vac", "retrieve: " + bigInteger);
//                setUserEth(bigInteger);
            }
        });
    }

    public void storePlayerEth(String value) {
        Web3j web3 = Web3j.build(new HttpService("https://goerli.infura.io/v3/f52c5ffe6f3249b3b5c553208d952fa1"));
        Credentials credentials = Credentials.create("405d238d5eb72504ff3f6f52a3ea0df6a27a378eeb5a3985901d06dfe4f6877a");
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        CardGameSolidity cardGameSolidity = CardGameSolidity.load(getKey(), web3, credentials, contractGasProvider);

        cardGameSolidity.store(new BigInteger(value)).flowable().subscribeOn(Schedulers.io()).subscribe(new Consumer<TransactionReceipt>() {
            @Override
            public void accept(TransactionReceipt transactionReceipt) throws Exception {
                Log.i("vac", "store: ");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, throwable.getMessage(), throwable);
            }
        });
    }

    public void increasePlayerEth(String value) {
        Web3j web3 = Web3j.build(new HttpService("https://goerli.infura.io/v3/f52c5ffe6f3249b3b5c553208d952fa1"));
        Credentials credentials = Credentials.create("405d238d5eb72504ff3f6f52a3ea0df6a27a378eeb5a3985901d06dfe4f6877a");
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        CardGameSolidity cardGameSolidity = CardGameSolidity.load(getKey(), web3, credentials, contractGasProvider);

        cardGameSolidity.increase(new BigInteger(value)).flowable().subscribeOn(Schedulers.io()).subscribe(new Consumer<TransactionReceipt>() {
            @Override
            public void accept(TransactionReceipt transactionReceipt) throws Exception {
                Log.i("vac", "increase: " + value);
//                BigInteger strToBigInt = new BigInteger(value);
//                setUserEth(getUserEth().add(strToBigInt));
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, throwable.getMessage(), throwable);
            }
        });
    }

    public void decreasePlayerEth(String value) {
        Web3j web3 = Web3j.build(new HttpService("https://goerli.infura.io/v3/f52c5ffe6f3249b3b5c553208d952fa1"));
        Credentials credentials = Credentials.create("405d238d5eb72504ff3f6f52a3ea0df6a27a378eeb5a3985901d06dfe4f6877a");
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        CardGameSolidity cardGameSolidity = CardGameSolidity.load(getKey(), web3, credentials, contractGasProvider);

        cardGameSolidity.decrease(new BigInteger(value)).flowable().subscribeOn(Schedulers.io()).subscribe(new Consumer<TransactionReceipt>() {
            @Override
            public void accept(TransactionReceipt transactionReceipt) throws Exception {
                Log.i("vac", "decrease: " + value);
//                BigInteger strToBigInt = new BigInteger(value);
//                setUserEth(getUserEth().subtract(strToBigInt));
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, throwable.getMessage(), throwable);
            }
        });
    }
}
