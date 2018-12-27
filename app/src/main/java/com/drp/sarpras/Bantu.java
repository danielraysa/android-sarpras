package com.drp.sarpras;

import java.util.ArrayList;

public class Bantu {

    public static final String DBNAME = "db_sarpras";

    public static final String sql_user = "CREATE TABLE IF NOT EXISTS User (" +
            "ID_USER INTEGER PRIMARY KEY, " +
            "NAMA_USER VARCHAR, " +
            "USERNAME VARCHAR, " +
            "PASSWORD VARCHAR, " +
            "NO_TELP VARCHAR, " +
            "ROLE VARCHAR);";

    public static final String sql_barang = "CREATE TABLE IF NOT EXISTS Barang (" +
            "ID_BARANG VARCHAR PRIMARY KEY, " +
            "NAMA_BARANG VARCHAR, " +
            "JENIS_BARANG VARCHAR, " +
            "STATUS VARCHAR " +
            ");";

    public static final String sql_siswa = "CREATE TABLE IF NOT EXISTS Siswa (" +
            "NIS VARCHAR PRIMARY KEY, " +
            "NAMA_SISWA VARCHAR, " +
            "NO_TELP VARCHAR, " +
            "ALAMAT VARCHAR " +
            ");";

    public static final String sql_peminjaman = "CREATE TABLE IF NOT EXISTS Peminjaman (" +
            "ID_PEMINJAMAN INTEGER PRIMARY KEY, " +
            "NIS VARCHAR, " +
            "TANGGAL_PINJAM DATE, " +
            "TANGGAL_KEMBALI DATE, " +
            "REAL_KEMBALI DATE, " +
            "STATUS VARCHAR" +
            ");";

    public static final String sql_peminjaman_detail = "CREATE TABLE IF NOT EXISTS Peminjaman_Detail (" +
            "ID_PEMINJAMAN INTEGER, " +
            "ID_BARANG VARCHAR" +
            ");";

    public static ArrayList<String[]> detailPeminjaman = new ArrayList<String[]>();

    public static String id_karyawan = "";
    public static String karyawan = "";
    public static ArrayList<String[]> detailPenjualan = new ArrayList<String[]>();
    public static ArrayList<String[]> detailPembelian = new ArrayList<String[]>();

    public static final String sql_pembelian = "CREATE TABLE IF NOT EXISTS Pembelian (" +
            "ID_PEMBELIAN VARCHAR PRIMARY KEY, " +
            "ID_KARYAWAN VARCHAR, " +
            "ID_SUPPLIER VARCHAR, " +
            "TANGGAL_PEMBELIAN DATE, " +
            "TOTAL DOUBLE," +
            "FOREIGN KEY(ID_KARYAWAN) REFERENCES Karyawan(ID_KARYAWAN), " +
            "FOREIGN KEY(ID_SUPPLIER) REFERENCES Supplier(ID_SUPPLIER) " +
            ");";

    public static final String sql_pembelian_detail = "CREATE TABLE IF NOT EXISTS Pembelian_detail (" +
            "ID_PEMBELIAN_DETAIL INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "ID_PEMBELIAN VARCHAR, " +
            "ID_BARANG VARCHAR, " +
            "HARGA DOUBLE, " +
            "JUMLAH INTEGER, " +
            "SUBTOTAL DOUBLE," +
            "FOREIGN KEY(ID_PEMBELIAN) REFERENCES Pembelian(ID_PEMBELIAN), " +
            "FOREIGN KEY(ID_BARANG) REFERENCES Barang(ID_BARANG) " +
            ");";

    public static final String sql_trigger_pembelian = "CREATE TRIGGER IF NOT EXISTS t_transaksi_pembelian AFTER INSERT ON Pembelian_detail " +
            "BEGIN " +
            "UPDATE Barang SET STOK = STOK + new.JUMLAH WHERE ID_BARANG = new.ID_BARANG; " +
            "END;";
}

