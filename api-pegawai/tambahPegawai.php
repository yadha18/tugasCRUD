<?php
 
include('koneksi.php');
 
$nama       = $_POST['nama'];
$no_pegawai = $_POST['no_pegawai'];
$alamat     = $_POST['alamat'];
$no_telp    = $_POST['no_telepon'];
 
if(!empty($nama) &amp;&amp; !empty($no_pegawai)){
 
    $sqlCheck = "SELECT COUNT(*) FROM tb_pegawai WHERE no_pegawai='$no_pegawai' AND nama='$nama'";
    $queryCheck = mysqli_query($conn,$sqlCheck);
    $hasilCheck = mysqli_fetch_array($queryCheck);
    if($hasilCheck[0] == 0){
        $sql = "INSERT INTO tb_pegawai (nama,no_pegawai,alamat,no_telepon) VALUES('$nama','$no_pegawai','$alamat','$no_telp')";
 
        $query = mysqli_query($conn,$sql);
 
        if(mysqli_affected_rows($conn) > 0){
            $data['status'] = true;
            $data['result'] = "Berhasil";
        }else{
            $data['status'] = false;
            $data['result'] = "Gagal";
        }
    }else{
        $data['status'] = false;
        $data['result'] = "Gagal, Data Sudah Ada";
    }
 
     
 
}
else{
    $data['status'] = false;
    $data['result'] = "Gagal, Nomor Pegawai dan Nama tidak boleh kosong!";
}
 
 
print_r(json_encode($data));
 
 
 
 
?>